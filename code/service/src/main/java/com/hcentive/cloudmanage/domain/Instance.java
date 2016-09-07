package com.hcentive.cloudmanage.domain;

public class Instance {
	
	private com.amazonaws.services.ec2.model.Instance awsInstance;
	private String dnsName;
	
	public Instance(com.amazonaws.services.ec2.model.Instance instance) {
		awsInstance = instance;
	}


	public com.amazonaws.services.ec2.model.Instance getAwsInstance() {
		return awsInstance;
	}

	public void setAwsInstance(com.amazonaws.services.ec2.model.Instance awsInstance) {
		this.awsInstance = awsInstance;
	}


	public String getDnsName() {
		return dnsName;
	}


	public void setDnsName(String dnsName) {
		this.dnsName = dnsName;
	}
	
}
