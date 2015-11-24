package com.hcentive.cloudmanage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hcentive.cloudmanage.domain.AWSClientProxy;

@Configuration
public class AWSConfig {

	@Bean
	public AWSClientProxy awsClientProxy() {
		return new AWSClientProxy();
	}
}
