package com.hcentive.cloudmanage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.hcentive.cloudmanage.utils.PasswordCryptoUtils;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:application-${env}.properties", ignoreResourceNotFound = true)
public class AppConfig {

	static {
		// A TTL of 60 seconds ensures that if there is a change in the IP
		// address that corresponds to an AWS resource, the JVM will refresh the
		// cached IP value after a relatively brief period of time.
		java.security.Security.setProperty("networkaddress.cache.ttl", "60");
	}

	public static String jenkinsUrl;

	public static String jenkinsPassword;

	public static String jenkinsUsername;

	public static String logBaseDir;

	public static String jenkinsJobsUrl;

	public static Integer lastDaysForHostNames;

	public static String billBaseDir;

	public static String billFileName;

	public static String billS3BucketName;

	public static String accountId;

	public static String accessKey;

	public static String secret;

	public static String adMgrPswd;

	public static String adMgrUserDN;

	public static String zeusUrl;

	public static String zeusPassword;

	public static String zeusUsername;

	public static boolean ingestBuildInfoEnabled;

	public static boolean ingestBillFilesEnabled;

	public static boolean ingestBillDataEnabled;

	public static boolean ingestCPUInfoEnabled;

	public static boolean ingestEC2MasterDataEnabled;

	public static boolean costEffectivenessEnabled;

	@Value("${jenkins.url}")
	public void setJenkinsUrl(String jenkinsUrl) {
		AppConfig.jenkinsUrl = jenkinsUrl;
	}

	@Value("${jenkins.password}")
	public void setJenkinsPassword(String jenkinsPassword) {
		AppConfig.jenkinsPassword = PasswordCryptoUtils
				.decryptPassword(jenkinsPassword);
	}

	@Value("${jenkins.username}")
	public void setJenkinsUsername(String jenkinsUsername) {
		AppConfig.jenkinsUsername = jenkinsUsername;
	}

	@Value("${zeus.url}")
	public void setZeusUrl(String zeusUrl) {
		AppConfig.zeusUrl = zeusUrl;
	}

	@Value("${zeus.password}")
	public void setZeusPassword(String zeusPassword) {
		AppConfig.zeusPassword = PasswordCryptoUtils
				.decryptPassword(zeusPassword);
	}

	@Value("${zeus.username}")
	public void setZeusUsername(String zeusUsername) {
		AppConfig.zeusUsername = zeusUsername;
	}

	@Value("${jenkins.log.base.dir}")
	public void setLogBaseDir(String logBaseDir) {
		AppConfig.logBaseDir = logBaseDir;
	}

	@Value("${jenkins.jobs.url}")
	public void setJenkinsJobsUrl(String jenkinsJobsUrl) {
		AppConfig.jenkinsJobsUrl = jenkinsJobsUrl;
	}

	@Value("${jenkins.lastDaysForHostNames}")
	public void setLastDaysForHostNames(String lastDaysForHostNames) {
		AppConfig.lastDaysForHostNames = new Integer(lastDaysForHostNames);
	}

	@Value("${aws.bill.base.dir}")
	public void setBillBaseDir(String billBaseDir) {
		AppConfig.billBaseDir = billBaseDir;
	}

	@Value("${aws.bill.file.name}")
	public void setBillFileName(String billFileName) {
		AppConfig.billFileName = billFileName;
	}

	@Value("${aws.accountId}")
	public void setAccountId(String accountId) {
		AppConfig.accountId = accountId;
	}

	@Value("${aws.billS3BucketName}")
	public void setBillS3BucketName(String billS3BucketName) {
		AppConfig.billS3BucketName = billS3BucketName;
	}

	@Value("${jenkins.lastDaysForHostNames}")
	public void setLastDaysForHostNames(Integer lastDaysForHostNames) {
		AppConfig.lastDaysForHostNames = lastDaysForHostNames;
	}

	@Value("${aws.bill.key}")
	public void setAccessKey(String accessKey) {
		AppConfig.accessKey = accessKey;
	}

	@Value("${aws.bill.secret}")
	public void setSecret(String secret) {
		AppConfig.secret = PasswordCryptoUtils.decryptPassword(secret);
	}

	@Value("${ad.managerPswd}")
	public void setAdMgrPswd(String adMgrPswd) {
		AppConfig.adMgrPswd = PasswordCryptoUtils.decryptPassword(adMgrPswd);
	}

	@Value("${ad.managerDN}")
	public void setAdMgrUserDN(String adMgrUserDN) {
		AppConfig.adMgrUserDN = adMgrUserDN;
	}

	@Value("${job.enable.ingest.build}")
	public void setIngestBuildInfoEnabled(boolean ingestBuildInfoEnabled) {
		AppConfig.ingestBuildInfoEnabled = ingestBuildInfoEnabled;
	}

	@Value("${job.enable.ingest.billFile}")
	public void setIngestBillFilesEnabled(boolean ingestBillFilesEnabled) {
		AppConfig.ingestBillFilesEnabled = ingestBillFilesEnabled;
	}

	@Value("${job.enable.ingest.billData}")
	public void setIngestBillDataEnabled(boolean ingestBillDataEnabled) {
		AppConfig.ingestBillDataEnabled = ingestBillDataEnabled;
	}

	@Value("${job.enable.ingest.cpu}")
	public void setIngestCPUInfoEnabled(boolean ingestCPUInfoEnabled) {
		AppConfig.ingestCPUInfoEnabled = ingestCPUInfoEnabled;
	}

	@Value("${job.enable.ingest.ec2}")
	public void setIngestEC2MasterDataEnabled(boolean ingestEC2MasterDataEnabled) {
		AppConfig.ingestEC2MasterDataEnabled = ingestEC2MasterDataEnabled;
	}

	@Value("${job.enable.costeffectiveness}")
	public void setCostEffectivenessEnabled(boolean costEffectivenessEnabled) {
		AppConfig.costEffectivenessEnabled = costEffectivenessEnabled;
	}

}
