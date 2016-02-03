package com.hcentive.cloudmanage;

import java.util.Calendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import com.hcentive.cloudmanage.billing.BillingService;
import com.hcentive.cloudmanage.domain.BuildJobResponse;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.JobInfo;
import com.hcentive.cloudmanage.domain.JobMetaInfo;
import com.hcentive.cloudmanage.jenkins.BuildInfoService;
import com.hcentive.cloudmanage.service.provider.aws.CloudWatchService;
import com.hcentive.cloudmanage.service.provider.aws.EC2Service;

// <Seconds> <Minutes> <Hours> <Day-of-Month> <Month> <Day-of-Week> [Year]
// <start from>/<every x units> for the above

@Configuration
@PropertySource("application.properties")
@PropertySource(value = "application-${env}.properties", ignoreResourceNotFound = true)
@EnableScheduling
public class IngestScheduler {

	private static final Logger logger = LoggerFactory
			.getLogger(IngestScheduler.class.getName());

	@Autowired
	private BuildInfoService jenkinsService;

	@Autowired
	private EC2Service ec2Service;

	@Autowired
	private BillingService billingService;

	@Autowired
	private CloudWatchService cloudWatchService;

	// Intended hourly
	@Scheduled(cron = "${jenkins.build.refresh.cron}")
	public void ingestHostNames() {
		try {
			BuildJobResponse builds = jenkinsService.getBuilds();
			for (JobMetaInfo job : builds.getJobs()) {
				String jobName = job.getName();
				if (jobName.contains("deploy")) {
					JobInfo jobInfo = jenkinsService.getJobInfo(jobName);
					logger.debug("Ingesting host data for job {} ", jobName);
					jenkinsService
							.updateHostNames(jobName, jobInfo.getBuilds());
					logger.info("Ingested host data for job {}", jobName);
				}
			}
		} catch (Exception e) {
			logger.error("Failed to ingest Host Names with error " + e);
		}
	}

	// Intended Monthly
	@Scheduled(cron = "${s3.bill.refresh.cron}")
	public void ingestBillingData() {
		Calendar now = Calendar.getInstance();
		int year = now.get(Calendar.YEAR);
		int previousMonth = now.get(Calendar.MONTH) - 1;
		try {
			logger.debug("Ingesting Billing data for {}-{} for account {}",
					previousMonth, year, AppConfig.accountId);
			billingService.updateBilling(AppConfig.accountId, year,
					previousMonth);
			logger.info("Ingested billing data for {}-{} for account {}",
					previousMonth, year, AppConfig.accountId);
		} catch (Exception e) {
			logger.error("Failed to ingest Billing Data for {}-{} with error "
					+ e.getMessage(), previousMonth, year);
		}
	}

	// Intended daily
	@Scheduled(cron = "${ec2.profile.refresh.cron}")
	public void ingestCPUUtilizationInfo() {
		try {
			logger.debug("Ingesting ec2 cpu data");
			List<Instance> ec2List = ec2Service.getInstanceLists();
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
			for (Instance ec2 : ec2List) {
				cloudWatchService.updateMetrics(ec2.getAwsInstance()
						.getInstanceId(), calendar.getTime(), dailyWindow
						.getTime());
			}
			logger.info("Ingested ec2 cpu data for " + calendar);
		} catch (Exception e) {
			logger.error("Failed to ingest ec2 cpu data with error " + e);
		}
	}

	// Intended hourly
	@Scheduled(cron = "${ec2.meta.refresh.cron}")
	public void ingestEC2MasterInfo() {
		try {
			logger.debug("Ingesting aws data for ec2");
			ec2Service.updateInstanceMetaInfo();
			logger.info("Ingested aws data for ec2");
		} catch (Exception e) {
			logger.error("Failed to ingest ec2 master info with error " + e);
		}
	}
}
