package com.hcentive.cloudmanage.domain;

import com.amazonaws.services.ec2.model.Vpc;

/**
 * @author vaibhav.gupta
 *
 */
public class VPC {
	
	public VPC(Vpc vpc){
		awsVpc = vpc;
	}

	private Vpc awsVpc;

	public Vpc getAwsVpc() {
		return awsVpc;
	}

	public void setAwsVpc(Vpc awsVpc) {
		this.awsVpc = awsVpc;
	}

	

}
