package com.hcentive.cloudmanage.service.provider.aws;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import com.amazonaws.services.ec2.AmazonEC2Client;

@Service
@Profile("prod")
public class AWSClientServiceImpl implements AWSClientService{

	@Override
	public AmazonEC2Client getClient() {
		return new AmazonEC2Client();
	}

}
