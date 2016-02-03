package com.hcentive.cloudmanage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import com.hcentive.cloudmanage.domain.AWSClientProxy;

@Configuration
@PropertySource("application.properties")
@PropertySource(value = "application-${env}.properties", ignoreResourceNotFound = true)
public class AWSConfig {

	@Bean
	public AWSClientProxy awsClientProxy() {
		return new AWSClientProxy();
	}
}
