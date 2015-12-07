package com.hcentive.cloudmanage.service.provider.aws;

import com.amazonaws.services.ec2.AmazonEC2Client;

public class AWSClientServiceImpl implements AWSClientService{

	@Override
	public AmazonEC2Client getClient() {
		return new AmazonEC2Client();
	}

}
