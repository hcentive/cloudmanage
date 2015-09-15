package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeVpcsRequest;
import com.amazonaws.services.ec2.model.DescribeVpcsResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Vpc;
import com.hcentive.cloudmanage.domain.VPC;
import com.hcentive.cloudmanage.service.provider.VPCService;

@Service
public class AWSVPCServiceImpl implements VPCService{
	
	@Autowired
	private DozerBeanMapper mapper;

	@Override
	public List<VPC> list() {
		
		AmazonEC2Client amazonClient = new AmazonEC2Client();
		DescribeVpcsRequest describeVpcsRequest = buildVPCRequest();
		DescribeVpcsResult result = amazonClient.describeVpcs(describeVpcsRequest);
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
		mapper.map(vpcs, vpcList);
		return vpcList;
	}

}
