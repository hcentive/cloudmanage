package com.hcentive.cloudmanage.service.provider.aws;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.amazonaws.services.elasticmapreduce.model.InstanceState;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.ResourceMetaData;
import com.hcentive.cloudmanage.service.GroupService;
import com.hcentive.cloudmanage.service.provider.InstanceService;

//@Service
public class AWSInstanceServiceImpl implements InstanceService {
	
	private static final String RUNNING = "running";

	private static final String TAG_COST_CENTER = "tag:cost-center";
	
	@Autowired
	private GroupService groupService;
	
	//@Autowired
	private AmazonEC2Client awsClient;

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
	public void stopInstances(String... instanceIDs) {
        StopInstancesRequest request = new StopInstancesRequest();
        request.withInstanceIds(instanceIDs);
        StopInstancesResult result = awsClient.stopInstances(request);
	}
	
	@Override
	public void startInstances(String... instanceIDs){
		StartInstancesRequest request = new StartInstancesRequest();
        request.withInstanceIds(instanceIDs);
        StartInstancesResult result = awsClient.startInstances(request);
	}
	
	@Override
	public void terminateInstances(String... instanceIDs) {
		TerminateInstancesRequest request = new TerminateInstancesRequest();
        request.withInstanceIds(instanceIDs);
        TerminateInstancesResult result = awsClient.terminateInstances(request);
	}
	
	@Override
	public List<Instance> list(List<String> groups) throws AccessDeniedException{
		validateGroups(groups);
		DescribeInstancesRequest describeInstancesRequest = buildInstancesRequest(groups);
		DescribeInstancesResult result = awsClient.describeInstances(describeInstancesRequest);
		List<Instance> instances= AWSUtils.extractInstances(result);
		return instances;
	}
	
	@Override
	public ResourceMetaData getInstancesMetaData() {
		List<Instance> instances = list();
		Map<String, Integer> regionsCount = getRegionsCount(instances);
		ResourceMetaData metaData = getResourceData(regionsCount);
		return metaData;
	}
	
	private ResourceMetaData getResourceData(
			Map<String, Integer> regionsCount) {
		Integer totalCount = 0;
		for(Entry<String, Integer> entry : regionsCount.entrySet()){
			Integer count = entry.getValue();
			totalCount = totalCount + count;
		}
		ResourceMetaData resourceMetaData = new ResourceMetaData();
		resourceMetaData.setCountByRegion(regionsCount);
		resourceMetaData.setTotal(totalCount);
		return resourceMetaData;
	}

	private Map<String, Integer> getRegionsCount(List<Instance> instances) {
		Map<String, Integer> regionsCount = new HashMap<String, Integer>();
		for(Instance instance : instances){
			com.amazonaws.services.ec2.model.Instance _instance = instance.getAwsInstance();
			if(_instance.getState().getName().equalsIgnoreCase(RUNNING)){
				String region = _instance.getPlacement().getAvailabilityZone();
				updateRegionCount(regionsCount, region);
			}
		}
		return regionsCount;
	}

	private void updateRegionCount(Map<String, Integer> regionsCount,
			String region) {
		Integer count = regionsCount.get(region);
		if(count == null){
			count = 0;
		}
		regionsCount.put(region, ++count);		
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
