package com.hcentive.cloudmanage.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeVpcsResult;
import com.amazonaws.services.ec2.model.Vpc;

@RestController
@RequestMapping("/vpcs")
public class VPCController {
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Vpc> list(){
		AmazonEC2Client amazonClient = new AmazonEC2Client();
		DescribeVpcsResult result = amazonClient.describeVpcs();
		return result.getVpcs();
	}
}
