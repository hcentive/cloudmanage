package com.hcentive.cloudmanage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;

//@Configuration
@Profile("dev")
public class DevConfiguration {
	
	@Value("${mock.endpoint}")
	private String endpoint;
	
	@Bean
	public AmazonEC2Client awsClient(){
		AmazonEC2Client amazonClient = new AmazonEC2Client(new BasicAWSCredentials("foo", "bar"));
		amazonClient.setEndpoint(endpoint);
		return amazonClient;
	}

}
