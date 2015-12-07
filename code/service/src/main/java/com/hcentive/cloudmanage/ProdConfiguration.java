package com.hcentive.cloudmanage;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.amazonaws.services.ec2.AmazonEC2Client;

//@Configuration
@Profile("prod")
public class ProdConfiguration {
	
	@Bean
	public AmazonEC2Client awsClient(){
		return new AmazonEC2Client();
	}

}
