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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.service.provider.aws.S3Service;

@Service("billingService")
public class BillingServiceImpl implements BillingService {

	@Autowired
	private S3Service s3bucketService;

	@Autowired
	private BillingInfoRepository billingInfoRepository;

	@Override
	public BillingInfo getBilling(String instanceId) {
		return null;
	}

	@Override
	public void updateBilling(String accountId, int year, int month) {
		File billDetailFile = readBillingInfo(accountId, year, month);
		List<BillingInfo> billLineItems = null;
		try {
			billLineItems = extractBill(billDetailFile);
			billingInfoRepository.save(billLineItems);
		} catch (IOException | ParseException e) {
			e.printStackTrace();
		}
	}

	private File readBillingInfo(String accountId, int year, int month) {
		if (accountId == null) {
			accountId = "770377720390";
			year = 2015;
			month = 12;
		}
		String FOLDER = "/Users/amit.kumar/workspace/IdentityBroker/bills/";
		String artifactName = accountId
				+ "-aws-billing-detailed-line-items-with-resources-and-tags-"
				+ year + "-" + month;
		return s3bucketService.getArtifact(FOLDER + artifactName);
	}

	public List<BillingInfo> extractBill(File billDetailFile)
			throws IOException, ParseException {
		Reader reader = new InputStreamReader(new FileInputStream(
				billDetailFile), "UTF-8");
		CSVParser parser = new CSVParser(reader, CSVFormat.EXCEL.withHeader());
		
		Map<String, DailyBillRecord> resources = new HashMap<String, DailyBillRecord>();
		
		for (CSVRecord record : parser) {
			String resourceId = record.get("ResourceId");
			// EC2 only ignore others
			if (resourceId.startsWith("i-")) {
				DailyBillRecord br = resources.get(resourceId);
				if (br == null) {
					br = new DailyBillRecord();
					//br.resourceId = resourceId;
					resources.put(resourceId, br);
				}

				// Get Date
				String timeStart = record.get("UsageStartDate");
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date day = df.parse(timeStart);
				// Get Previous Cost
				//BigDecimal cost = br.costPerDay.get(day);
				//if (cost == null) {
					//cost = new BigDecimal(0);
				//}
				// Add to existing cost
				BigDecimal addedCost = new BigDecimal(
						record.get("UnBlendedCost"));
				//cost = cost.add(addedCost);
				// Update Cost
				//br.costPerDay.put(day, cost);
			}
		}
		return null;
	}

}
