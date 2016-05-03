package com.hcentive.cloudmanage.billing;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.AppConfig;
import com.hcentive.cloudmanage.service.provider.aws.S3Service;

@Service("billingService")
public class BillingServiceImpl implements BillingService {

	@Autowired
	private S3Service s3bucketService;

	@Autowired
	private BillingInfoRepository billingInfoRepository;

	@Autowired
	private AWSMetaRepository awsMetaRepository;

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
	public List<BillingInfo> getBilling(Date fromTime,
			Date tillTime) {
		return billingInfoRepository.findByPeriod(fromTime,
				tillTime);
	}

	@Override
	public void updateBilling(String accountId, int year, int month)
			throws IOException {
		year = 2015;
		month = 1;
		// Get the file from S3 if not available.
		File billDetailFile = readBillingInfo(accountId, year, month);
		// Extract and save Data
		List<BillingInfo> billLineItems = null;
		try {
			billLineItems = extractBill(billDetailFile, year, month);
			billingInfoRepository.save(billLineItems);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private File readBillingInfo(String accountId, int year, int month)
			throws IOException {
		// Check local folder
		String artifactName =  AppConfig.billBaseDir + '/' + AppConfig.billFileName;
		artifactName = artifactName.replace("<year>", String.valueOf(year));
		artifactName = artifactName.replace("<month>", String.valueOf(month));
		File billFile = new File(artifactName);
		// fetch from S3 if not available.
		if (!billFile.exists()) {
			logger.info(
					"Bill file {} not available ... getting form S3 bucket",
					artifactName);
			billFile = s3bucketService.getBill(AppConfig.billS3BucketName,
					artifactName, AppConfig.billBaseDir);
			logger.info("Bill file now available at {} ",
					billFile.getAbsolutePath());
		}
		return billFile;
	}

	private List<BillingInfo> extractBill(File billDetailFile, int year,
			int month) throws IOException, ParseException {

		List<BillingInfo> billLineItems = new ArrayList<BillingInfo>();

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
}
