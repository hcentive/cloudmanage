package com.hcentive.cloudmanage;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.hcentive.cloudmanage.audit.AuditContext;
import com.hcentive.cloudmanage.audit.AuditContextHolder;
import com.hcentive.cloudmanage.billing.BillingService;
import com.hcentive.cloudmanage.domain.BuildJobResponse;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.JobInfo;
import com.hcentive.cloudmanage.domain.JobMetaInfo;
import com.hcentive.cloudmanage.jenkins.BuildInfoService;
import com.hcentive.cloudmanage.service.provider.aws.AWSUtils;
import com.hcentive.cloudmanage.service.provider.aws.CloudWatchService;
import com.hcentive.cloudmanage.service.provider.aws.EC2Service;
import com.hcentive.cloudmanage.service.provider.aws.S3Service;

// <Seconds> <Minutes> <Hours> <Day-of-Month> <Month> <Day-of-Week> [Year]
// <start from>/<every x units> for the above

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:application-${env}.properties", ignoreResourceNotFound = true)
@EnableScheduling
public class IngestScheduler {

	private static final Logger logger = LoggerFactory
			.getLogger(IngestScheduler.class.getName());

	private boolean jobContext = true;

	@Autowired
	private BuildInfoService jenkinsService;

	@Autowired
	private EC2Service ec2Service;

	@Autowired
	private BillingService billingService;
	@Autowired
	private S3Service s3bucketService;

	@Autowired
	private CloudWatchService cloudWatchService;

	// Intended hourly
	@Scheduled(cron = "${jenkins.build.refresh.cron}")
	public void ingestHostNames() {
		if (!AppConfig.ingestBuildInfoEnabled) {
			logger.info("Ingest host data for jenkins jobs skipped");
			return;
		}
		try {
			BuildJobResponse builds = jenkinsService.getBuilds();
			int buildSize = builds.getJobs().size();
			int count = 0;
			for (JobMetaInfo job : builds.getJobs()) {
				String jobName = job.getName();
				count++;
				if (jobName.contains("deploy")) {
					try {
						JobInfo jobInfo = jenkinsService.getJobInfo(jobName);
						logger.debug(
								"Ingesting host data for job {}; {} of {}",
								jobName, count, buildSize);
						jenkinsService.updateHostNames(jobName,
								jobInfo.getBuilds());
						logger.info("Ingested host data for job {}; {} of {}",
								jobName, count, buildSize);
					} catch (Exception e) {
						logger.error(
								"Skipping: Failed to ingest host data for job {}:{} ",
								jobName, e);
					}
				}
			}
		} catch (Exception e) {
			logger.error(
					"Failed to ingest Host Names for this run with error {}", e);
		}
		logger.info("Ingested host data for jenkins jobs");
	}

	// Intended Monthly
	@Scheduled(cron = "${s3.bill.sync.cron}")
	public void syncBillingData() {
		if (!AppConfig.ingestBillFilesEnabled) {
			logger.info("Ingest bill files for bill job skipped");
			return;
		}
		try {
			logger.debug("Sync bill info for account {}", AppConfig.accountId);
			// Get list of files ingested from database.
			List<String> ingested = billingService.billsIngested();
			// List of available files on S3 limits to 1000 - good for now
			List<String> filesAvailable = s3bucketService.getBucketList(
					AppConfig.billS3BucketName, "bill");
			// Regex removal of files not matching pattern.
			List<String> matches = new ArrayList<String>();
			Pattern p = Pattern.compile(AppConfig.billFileName);
			for (String fileAvailable : filesAvailable) {
				if (p.matcher(fileAvailable).matches()) {
					matches.add(fileAvailable);
				}
			}
			// Now remove already ingested files
			matches.removeAll(ingested);
			String fileDest = AppConfig.billBaseDir + "/ingest";
			for (String billFile : matches) {
				s3bucketService.getBill(AppConfig.billS3BucketName, billFile,
						fileDest);
			}
			logger.info("Sync'd bill files for account {}", AppConfig.accountId);
		} catch (Exception e) {
			logger.error("Failed to synchronize bill files for {} with error "
					+ e.getMessage(), AppConfig.accountId);
		}
	}

	// Can be scheduled on need basis.
	@Scheduled(cron = "${s3.bill.ingest.cron}")
	public void ingestBillingData() {
		if (!AppConfig.ingestBillDataEnabled) {
			logger.info("Ingest bill data for bill job skipped");
			return;
		}
		String billsLocation = AppConfig.billBaseDir + "/ingest";
		logger.info("Ingest bill info from {}", billsLocation);

		// List all files and ingest 1-by-1 if already not ingested
		// Its a double check but necessary
		// Get list of files ingested from database.
		List<String> ingested = billingService.billsIngested();

		// On file system
		File folder = new File(billsLocation);
		File[] listOfFiles = folder.listFiles();
		for (File file : listOfFiles) {
			if (!file.isHidden() && !ingested.contains(file.getName() + ".zip")) {
				try {
					logger.info("Ingest bill from {} ", file.getName());
					billingService.updateBilling(file);
					// Update the table that the file has been ingeted.
					billingService.markBillIngested(file.getName() + ".zip");
					logger.info("Ingested bill from {}", file.getName());
				} catch (Exception e) {
					logger.error("Failed to ingest bill file {} with error "
							+ e.getMessage(), AppConfig.accountId);
					e.printStackTrace();
				}
			}
		}
		logger.info("Ingested bill info from {}", billsLocation);
	}

	// Intended daily
	@Scheduled(cron = "${ec2.profile.refresh.cron}")
	public void ingestCPUUtilizationInfo() {
		if (!AppConfig.ingestCPUInfoEnabled) {
			logger.info("Ingest ec2 cpu job skipped");
			return;
		}
		try {
			logger.debug("Ingesting ec2 cpu data");
			List<Instance> ec2List = ec2Service.getInstanceLists(jobContext);
			// Todays data
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int yesterday = calendar.get(Calendar.DATE) - 1;
			// Consume yesterdays data.
			calendar.set(year, month, yesterday, 0, 0, 0);

			Calendar dailyWindow = Calendar.getInstance();
			dailyWindow.set(year, month, yesterday, 23, 59, 59);
			// Loop for all instances.
			int counter = 0;
			int totalCount = ec2List.size();
			for (Instance ec2 : ec2List) {
				try {
					cloudWatchService.updateMetrics(ec2.getAwsInstance()
							.getInstanceId(), calendar.getTime(), dailyWindow
							.getTime());
					logger.info("Profile Info Ingested {} out of {}",
							counter++, totalCount);
				} catch (Exception e) {
					logger.error(
							"Skipping: Failed to ingest cpu data for {}:{} ",
							ec2.getAwsInstance().getInstanceId(), e);
				}
			}
			logger.info("Ingested ec2 cpu data for " + calendar);
		} catch (Exception e) {
			logger.error("Failed to ingest ec2 cpu data with error " + e);
		}
	}

	// Intended hourly
	@Scheduled(cron = "${ec2.meta.refresh.cron}")
	public void ingestEC2MasterInfo() {
		if (!AppConfig.ingestEC2MasterDataEnabled) {
			logger.info("Ingest ec2-master data job skipped");
			return;
		}
		try {
			logger.info("Ingesting aws data for ec2");
			ec2Service.updateInstanceMetaInfo(jobContext);
			logger.info("Ingested aws data for ec2");
		} catch (Exception e) {
			logger.error("Failed to ingest ec2 master info with error " + e);
		}
	}

	// Intended hourly
	@Scheduled(cron = "${ec2.cost.effectiveness.cron}")
	public void costEffectivenessEC2() {
		if (!AppConfig.costEffectivenessEnabled) {
			logger.info("Ensuring cost effectiveness job skipped");
			return;
		}
		try {
			logger.info("Ensuring Cost effectiveness for ec2");
			List<Instance> ec2List = ec2Service.getRunningInstances(jobContext);
			// last 24 hours.
			Calendar now = Calendar.getInstance();
			now.add(Calendar.HOUR_OF_DAY, -24);
			Date yesterday = now.getTime();
			// Now filter out EC2 started less than 24 hours.
			StringBuilder strBld = new StringBuilder();
			for (Iterator<Instance> it = ec2List.iterator(); it.hasNext();) {
				Instance ec2 = it.next();
				if (ec2.getAwsInstance().getLaunchTime().after(yesterday)) {
					logger.info("Skip the new instance {}; create time {}", ec2
							.getAwsInstance().getInstanceId(), ec2
							.getAwsInstance().getLaunchTime());
					it.remove();
				} else {
					strBld.append(ec2.getAwsInstance().getInstanceId()).append(
							", ");
				}
			}
			logger.info("Manage effectiveness for {}.", strBld.toString());
			ec2List = cloudWatchService.getIneffectiveInstances(ec2List);
			for (Instance ec2 : ec2List) {
				String instanceId = ec2.getAwsInstance().getInstanceId();
				logger.info("Ineffective instances {}", instanceId);
				StopInstancesResult stoppedResult = ec2Service.stopInstance(
						instanceId, false);
				if (stoppedResult != null) {
					AuditContext auditContext = new AuditContext();
					auditContext.setInitiator(instanceId
							+ "_AttemptStopUnutlized-ec2");
					AuditContextHolder.setContext(auditContext);
					logger.info("Saved by stopping the instance {} : {}",
							instanceId, stoppedResult);
				} else {
					logger.info("Skipped stopping the instance {} : {}",
							instanceId, stoppedResult);
				}
			}
			logger.info("Ensured Cost effectiveness for ec2 {} complete",
					ec2List.size());
		} catch (Exception e) {
			logger.error("Failed to ensure ec2 cost effectiveness with error "
					+ e);
			e.printStackTrace();
		}
	}
}
