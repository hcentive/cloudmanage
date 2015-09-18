package com.hcentive.cloudmanage.service.provider.aws;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Reservation;
import com.hcentive.cloudmanage.domain.Group;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.service.group.GroupService;
import com.hcentive.cloudmanage.service.provider.InstanceService;

@Service
public class AWSInstanceServiceImpl implements InstanceService {
	
	private static final String TAG_COST_CENTER = "tag:cost-center";
	
	@Autowired
	private GroupService groupService;

	@Override
	public List<Instance> list(){
		List<String> groups = groupService.getGroups();
		List<Instance> instances = null;
		try {
			instances = list(groups);
		} catch (AccessDeniedException e) {
			//Exception cannot come
			e.printStackTrace();
		}
		
		return instances;
	}
	
	@Override
	public List<Instance> list(List<String> groups) throws AccessDeniedException{
		validateGroups(groups);
		AmazonEC2Client amazonClient = new AmazonEC2Client();		
		DescribeInstancesRequest describeInstancesRequest = buildInstancesRequest(groups);
		DescribeInstancesResult result = amazonClient.describeInstances(describeInstancesRequest);
		List<Instance> instances= extractInstances(result);
		return instances;
		
		
	}
	
	private void validateGroups(List<String> groups) throws AccessDeniedException {
		List<String> _groups = groupService.getGroups();
		for(String group: groups){
			if(! _groups.contains(group)){
				throw new AccessDeniedException("Invalid group " + group);
			}
		}
		
	}

	private List<Instance> extractInstances(DescribeInstancesResult result) {
		List<Reservation> reservations=result.getReservations();
		List<Instance> instances= new ArrayList<Instance>();
		for (Reservation reservation : reservations) {
		    for (com.amazonaws.services.ec2.model.Instance instance : reservation.getInstances()) {
		    	Instance _instance = new Instance(instance);
		    	instances.add(_instance);
		    }
		  } 
		return instances;
	}

	private DescribeInstancesRequest buildInstancesRequest(List<String> groups){
		DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest();
		List<Filter> filters = createRequestFilters(groups);
		describeInstancesRequest.setFilters(filters);
		return describeInstancesRequest;
	}

	private List<Filter> createRequestFilters(List<String> groups) {
		List<Filter> filters = new ArrayList<Filter>();
		Filter f1 = new Filter(TAG_COST_CENTER);
		f1.setValues(groups);
		filters.add(f1);
		return filters;
	}

}
