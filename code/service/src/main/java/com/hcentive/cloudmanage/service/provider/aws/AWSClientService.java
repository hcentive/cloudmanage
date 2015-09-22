package com.hcentive.cloudmanage.service.provider.aws;

import com.amazonaws.services.ec2.AmazonEC2Client;

public interface AWSClientService {
	
	AmazonEC2Client getClient();

}
