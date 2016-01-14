package com.hcentive.cloudmanage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

	static {
		// A TTL of 60 seconds ensures that if there is a change in the IP
		// address that corresponds to an AWS resource, the JVM will refresh the
		// cached IP value after a relatively brief period of time.
		java.security.Security.setProperty("networkaddress.cache.ttl", "60");
		System.setProperty("javax.net.ssl.trustStore", "jenkinsKeystore");
		System.setProperty("javax.net.ssl.trustStorePassword", "jenkins");
	}

	@Value("${jenkins.url}")
	public static String jenkinsUrl;
	
	@Value("${jenkins.password}")
	public static String jenkinsPassword;
	
	@Value("${jenkins.username}")
	public static String jenkinsUsername;

	@Value("${jenkins.log.base.dir}")
	public static String logBaseDir;
	
	@Value("${jenkins.jobs.url}")
	public static String jenkinsJobsUrl;
}
