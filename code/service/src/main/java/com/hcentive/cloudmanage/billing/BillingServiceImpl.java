package com.hcentive.cloudmanage.billing;

import com.hcentive.cloudmanage.service.provider.aws.S3Service;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("billingService")
public class BillingServiceImpl implements BillingService {

	@Autowired
	private S3Service s3bucketService;

	@Autowired
	private BillingInfoRepository billingInfoRepository;

	@Autowired
	private AWSMetaRepository awsMetaRepository;
	@Autowired
	private BillFileInfoRepository billingFileInfoRepository;

	private final DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private static final Logger logger = LoggerFactory
			.getLogger(BillingServiceImpl.class.getName());

	// fetch for specific instance.
	@Override
	public List<BillingInfo> getBilling(String instanceId, Date fromTime,
			Date tillTime) {
		return billingInfoRepository.findByInstanceId(instanceId, fromTime,
				tillTime);
	}

	@Override
	public List<BillingInfo> getBilling(Date fromTime, Date tillTime) {
		return billingInfoRepository.findByPeriod(fromTime, tillTime);
	}

	@Override
	public void updateBilling(File billFile) throws IOException {
		// Extract and save Data
		List<BillingInfo> billLineItems = null;
		try {
			logger.info("Ingest records from " + billFile);
			billLineItems = extractBill(billFile);
			for (BillingInfo billLineItem : billLineItems) {
				try {
					billingInfoRepository.save(billLineItem);
				} catch (DataIntegrityViolationException e) {
					logger.warn("Skipping {} as it could not be saved for {}",
							billLineItem.toString(), e.getMessage());
				}
			}
			logger.info("All records updated for " + billFile);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			logger.error("Failed to Ingest records from {} for {}", billFile, e);
		}
	}

	private List<BillingInfo> extractBill(File billDetailFile)
			throws IOException, ParseException {

		List<BillingInfo> billLineItems = new ArrayList<BillingInfo>();
		// Extract Year and Month from file name pattern.
		Pattern p = Pattern.compile("-\\d{4}-\\d{2}");
		Matcher m = p.matcher(billDetailFile.getName());
		int year = 0, month = 0;
		if (m.find()) {
			StringTokenizer strTkn = new StringTokenizer(m.group(0));
			year = Integer.parseInt(strTkn.nextToken("-"));
			month = Integer.parseInt(strTkn.nextToken("-"));
		} else {
			throw new IOException(
					"The file name does not conatin pattern -YYYY-MM and hence can not be ingested");
		}

		Reader reader = new InputStreamReader(new FileInputStream(
				billDetailFile), "UTF-8");
		CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
		// Convenient Map with Daily Billing record per instance.
		Map<String, List<BigDecimal>> resources = new HashMap<String, List<BigDecimal>>();

		try {
			for (CSVRecord record : parser) {
				String resourceId = record.get("ResourceId");
				// EC2 only ignore others
				if (resourceId.startsWith("i-")) {
					// Instantiate a List of total for this EC2
					List<BigDecimal> brList = resources.get(resourceId);
					if (brList == null) {
						// There are 31 days in a month - 1 for each day.
						brList = new ArrayList<BigDecimal>(32);
						for (int i = 1; i <= 32; i++) {
							brList.add(null);
						}
						resources.put(resourceId, brList);
					}

					// Get Date and use as index of the arrayList of total.
					String timeStart = record.get("UsageStartDate");
					Calendar cal = Calendar.getInstance();
					cal.setTime(df.parse(timeStart));
					// Get Cost
					BigDecimal addedCost = new BigDecimal(
							record.get("UnBlendedCost"));
					int index = cal.get(Calendar.DAY_OF_MONTH);
					// Instantiate individual total for this day
					if (brList.get(index) == null) {
						brList.set(index, new BigDecimal("000.000000"));
					}

					// Add to total
					brList.set(index, brList.get(index).add(addedCost));
				}
			}
		} finally {
			parser.close();
		}
		logger.debug("All records read as " + resources);
		// Get all ec2 info
		Iterable<AWSMetaInfo> ec2List = awsMetaRepository.findAll();
		Map<String, AWSMetaInfo> ec2Map = new HashMap<String, AWSMetaInfo>();
		for (AWSMetaInfo ec2 : ec2List) {
			ec2Map.put(ec2.getAwsInstanceId(), ec2);
		}
		// Create objects to persist
		Calendar cal = Calendar.getInstance();
		// Day of the month will be updated down below in the loop.
		cal.set(year, month, 0, 0, 0, 0);
		for (String resource : resources.keySet()) {
			AWSMetaInfo ec2 = ec2Map.get(resource);
			if (ec2 == null) {
				logger.warn("EC2 resource does not exists " + resource);
				continue;
			}
			List<BigDecimal> billingList = resources.get(resource);
			for (int dayOfTheMonth = 1; dayOfTheMonth <= 31; dayOfTheMonth++) {
				BillingInfo billingInfo = new BillingInfo();
				billingInfo.setInstanceInfo(ec2);
				BigDecimal dayTotal = billingList.get(dayOfTheMonth);
				billingInfo.setDayTotal(dayTotal);
				cal.set(Calendar.DAY_OF_MONTH, dayOfTheMonth);
				billingInfo.setSnapshotAt(cal.getTime());
				// Add to list of line items.
				billLineItems.add(billingInfo);
			}
		}
		return billLineItems;
	}

	@Override
	public List<String> billsIngested() {
		return billingFileInfoRepository.getBillsIngested();
	}

	@Override
	public void markBillIngested(String billFile) {
		BillFileInfo billFileInfo = new BillFileInfo(billFile);
		billingFileInfoRepository.save(billFileInfo);
	}

	@Override
	public BigDecimal getBillingCost(String instanceId, Date fromDate, Date toDate) {
		List<BillingInfo> billingInfoList = getBilling(instanceId,fromDate,toDate);
		return billingInfoList.stream()
					.filter(billingInfo -> billingInfo.getDayTotal() != null)
					.map(BillingInfo::getDayTotal)
					.reduce(BigDecimal.ZERO,BigDecimal::add);
	}

	@Override
	public Map<String, Map<String, BigDecimal>> getBillingCostByClientStack(Date fromDate, Date toDate) {
		List<BillingInfo> billingInfoList = billingInfoRepository.findByPeriod(fromDate,toDate);
		Map<String,Map<String,BigDecimal>> billingCostByClient = new HashMap<>();
		Map<String, BigDecimal> billingCostByStack;
		BillingInfo billingInfo;
		String clientName;
		String stack;
		Iterator<BillingInfo> billingInfoIterator = billingInfoList.iterator();
		while (billingInfoIterator.hasNext()){
			billingInfo = billingInfoIterator.next();
			clientName = getClientName(billingInfo);
			stack = getStack(billingInfo);
			if(billingCostByClient.containsKey(clientName)){
				// client present
				billingCostByStack = billingCostByClient.get(clientName);
				if(billingCostByStack.containsKey(stack)){
					// Stack present
					BigDecimal stackCost = billingCostByStack.get(stack);
					billingCostByStack.put(stack,stackCost.add(getCost(billingInfo)));
				}else{
					// Stack not present
					billingCostByStack.put(stack,getCost(billingInfo));
				}
			}else{
				// client not present
				billingCostByStack = new HashMap<>();
				billingCostByStack.put(stack,getCost(billingInfo));
				billingCostByClient.put(clientName,billingCostByStack);
			}
		}
		return billingCostByClient;
	}

	@Override
	public Map<String, Map<String, BigDecimal>> getBillingTrendByClientTime(Date fromDate, Date toDate) {
		List<BillingInfo> billingInfoList = billingInfoRepository.findByPeriod(fromDate,toDate);
		Map<String,Map<String,BigDecimal>> billingTrendByClient = new HashMap<>();
		Map<String, BigDecimal> billingTrendByTimeDuration;
		BillingInfo billingInfo;
		String clientName;
		String dateString;
		Iterator<BillingInfo> billingInfoIterator = billingInfoList.iterator();
		DateFormat dateFormat = new SimpleDateFormat(getTimeDurationFormat(fromDate,toDate));

		while (billingInfoIterator.hasNext()){
			billingInfo = billingInfoIterator.next();
			clientName = getClientName(billingInfo);
			dateString = dateFormat.format(billingInfo.getSnapshotAt());
			if(billingTrendByClient.containsKey(clientName)){
				// client present
				billingTrendByTimeDuration = billingTrendByClient.get(clientName);
				if(billingTrendByTimeDuration.containsKey(dateString)){
					// DateString present
					BigDecimal stackCost = billingTrendByTimeDuration.get(dateString);
					billingTrendByTimeDuration.put(dateString,stackCost.add(getCost(billingInfo)));
				}else{
					// DateString not present
					billingTrendByTimeDuration.put(dateString,getCost(billingInfo));
				}
			}else{
				// client not present
				billingTrendByTimeDuration = getTrendByTimeHashMap(fromDate,toDate);
				billingTrendByTimeDuration.put(dateString,getCost(billingInfo));
				billingTrendByClient.put(clientName,billingTrendByTimeDuration);
			}
		}
		return billingTrendByClient;
	}

	private String getStack(BillingInfo billingInfo){
		String stackValue = "Unknown".toLowerCase();
		if(billingInfo.getInstanceInfo().getStack() == null || billingInfo.getInstanceInfo().getStack().isEmpty()){
			return stackValue;
		}else{
			return billingInfo.getInstanceInfo().getStack().toLowerCase();
		}
	}

	private String getClientName(BillingInfo billingInfo){
		String clientName = "Unknown".toLowerCase();
		if(billingInfo.getInstanceInfo().getClient()== null || billingInfo.getInstanceInfo().getClient().isEmpty()){
			return clientName;
		}else{
			return billingInfo.getInstanceInfo().getClient().toLowerCase();
		}
	}

	private BigDecimal getCost(BillingInfo billingInfo){
		return billingInfo.getDayTotal() == null ? BigDecimal.ZERO : billingInfo.getDayTotal().setScale(3,BigDecimal.ROUND_HALF_EVEN);
	}

	private String getTimeDurationFormat(Date fromDate, Date toDate) {
		String timeDurationFormat = "yyyyMMdd";
		DateFormat dateFormat = new SimpleDateFormat(timeDurationFormat);
		Long from = Long.valueOf(dateFormat.format(fromDate));
		Long to = Long.valueOf(dateFormat.format(toDate));
		Long duration = to - from;
		if(duration > 30 ){
			timeDurationFormat = "yyyyMM";
		}
		return timeDurationFormat;
	}

	private Map<String,BigDecimal> getTrendByTimeHashMap(Date fromDate, Date toDate) {
		Map<String,BigDecimal> trendByTimeMap = new HashMap<>();
		String dateString;
		String timeDurationFormat = getTimeDurationFormat(fromDate,toDate);
		boolean addDay = timeDurationFormat.contains("dd");
		DateTime from = new DateTime(fromDate);
		DateTime to = new DateTime(toDate);
		DateFormat dateFormat = new SimpleDateFormat(timeDurationFormat);
		while (from.isBefore(to)){
			dateString = dateFormat.format(from.toDate());
			trendByTimeMap.put(dateString,BigDecimal.ZERO);
			if(addDay){
				from = from.plusDays(1);
			}else{
				from = from.plusMonths(1);
			}
		}
		return trendByTimeMap;
	}
}
