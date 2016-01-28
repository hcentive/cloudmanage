package com.hcentive.cloudmanage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.hcentive.cloudmanage.utils.PasswordCryptoUtils;

@Configuration
public class AppConfig {

	static {
		// A TTL of 60 seconds ensures that if there is a change in the IP
		// address that corresponds to an AWS resource, the JVM will refresh the
		// cached IP value after a relatively brief period of time.
		java.security.Security.setProperty("networkaddress.cache.ttl", "60");
		// System.setProperty("javax.net.ssl.trustStore", "jenkinsKeystore");
		// System.setProperty("javax.net.ssl.trustStorePassword", "jenkins");
	}

	public static String jenkinsUrl;

	public static String jenkinsPassword;

	public static String jenkinsUsername;

	public static String logBaseDir;

	public static String jenkinsJobsUrl;

	public static Integer lastDaysForHostNames;

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

	@Value("${jenkins.log.base.dir}")
	public void setLogBaseDir(String logBaseDir) {
		AppConfig.logBaseDir = logBaseDir;
	}

	@Value("${jenkins.jobs.url}")
	public void setJenkinsJobsUrl(String jenkinsJobsUrl) {
		AppConfig.jenkinsJobsUrl = jenkinsJobsUrl;
	}

	@Value("${jenkins.lastDaysForHostNames}")
	public static void setLastDaysForHostNames(String lastDaysForHostNames) {
		AppConfig.lastDaysForHostNames = new Integer(lastDaysForHostNames);
	}

}
