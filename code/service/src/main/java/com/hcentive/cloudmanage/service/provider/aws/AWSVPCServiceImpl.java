package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeVpcsRequest;
import com.amazonaws.services.ec2.model.DescribeVpcsResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Vpc;
import com.hcentive.cloudmanage.domain.VPC;
import com.hcentive.cloudmanage.service.provider.VPCService;

//@Service
public class AWSVPCServiceImpl implements VPCService{
	
	@Autowired
	private AmazonEC2Client awsClient;
	
	@Override
	public List<VPC> list() {
		
		DescribeVpcsRequest describeVpcsRequest = buildVPCRequest();
		DescribeVpcsResult result = awsClient.describeVpcs(describeVpcsRequest);
		List<VPC> vpcs = convertAWSVPCToVPC(result.getVpcs());
		return vpcs;
	}

	private DescribeVpcsRequest buildVPCRequest() {
		DescribeVpcsRequest describeVpcsRequest = new DescribeVpcsRequest();
		List<Filter> filters = getRequestFilters();
		describeVpcsRequest.setFilters(filters);
		return describeVpcsRequest;
	}

	private List<Filter> getRequestFilters(){
		List<Filter> filters = new ArrayList<Filter>();
		return filters;
	}
	
	private List<VPC> convertAWSVPCToVPC(List<Vpc> vpcs) {
		List<VPC> vpcList = new ArrayList<VPC>();
		for(Vpc vpc : vpcs){
			VPC _vpc = new VPC(vpc);
			vpcList.add(_vpc);
		}
		return vpcList;
	}

}
