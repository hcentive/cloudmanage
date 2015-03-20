package com.hcentive.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.Tag;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.opsworks.model.StopInstanceRequest;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Multiset;
import com.google.common.collect.TreeMultiset;
import com.hcentive.domain.ServerInstance;

@Service("manageService")
public class ServerInstanceManageServiceImpl 
{
    @Autowired
	private AmazonEC2  ec2;
    private static final Set<String> INSTANCE_STATE_IN_PROGRESS_SET = ImmutableSet.<String> builder()
            .add("pending", "shutting-down", "stopping").build();
    private static final Set<String> INSTANCE_STATE_COMPLETE_SET = ImmutableSet.<String> builder()
            .add("running", "terminated", "stopped").build();
    public static final int DEFAULT_AWAIT_TRANSITION_INTERVAL = 2000;
    
    //Note: Exception handling need to complete yet
    
    public List<ServerInstance> renderDashboardPage() throws AmazonServiceException
    {
    	DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<String> valuesT1 = new ArrayList<String>();
        valuesT1.add("subnet-1b1fce30");
        Filter filter = new Filter().withName("subnet-id").withValues(valuesT1);

        DescribeInstancesResult result = ec2.describeInstances(request.withFilters(filter));   
       // DescribeInstancesResult describeInstancesRequest = ec2.describeInstances();
       List<Reservation> reservations=result.getReservations();
        Set<Instance> instances = new HashSet<Instance>();

        for (Reservation reservation : reservations) {
            instances.addAll(reservation.getInstances());
        }
        List<ServerInstance> instanceList=new LinkedList<ServerInstance>();
        for(Instance i:instances)
        {
        	ServerInstance instance=new ServerInstance();
        	instance.setInstanceId(i.getInstanceId());
        	setInstanceNameAndOwnerByTags(i.getTags(),instance);
        	instance.setCreatetime(i.getNetworkInterfaces().get(0).getAttachment().getAttachTime());
        	instance.setState(i.getState().getName());
        	instanceList.add(instance);
        }
    	return instanceList;
    }
	
    public ServerInstance refreshServerInstance(String instanceId) throws AmazonServiceException
    {
    	DescribeInstancesResult result=filterResultWithInstanceId(instanceId);
        ServerInstance instance=new ServerInstance();
    	List<Instance> instanceList=extractInstances(result);
        for (Instance i : instanceList) {
        	instance.setInstanceId(i.getInstanceId());
        	setInstanceNameAndOwnerByTags(i.getTags(),instance);
        	instance.setCreatetime(i.getLaunchTime());
        	instance.setState(i.getState().getName());
        }
    	return instance;
    }
    
    public String startServerInstance(String instanceId) 
    {
    	//DescribeInstancesResult result=filterResultWithInstanceId(instanceId);
    	StartInstancesRequest startRequest = new StartInstancesRequest().withInstanceIds(instanceId);
    	ec2.startInstances(startRequest);
    	Set<String> instanceStatus= waitForTransitionCompletion("running", instanceId);
    	if(instanceStatus.contains("running"))
    		return "running";
    	else
    		return "error";
    }
    public String stopServerInstance(String instanceId)
    {
    	StopInstancesRequest stopRequest = new StopInstancesRequest().withInstanceIds(instanceId);
    	ec2.stopInstances(stopRequest);
    	Set<String> instanceStatus= waitForTransitionCompletion("stopped", instanceId);
    	if(instanceStatus.contains("stopped"))
    		return "stopped";
    	else
    		return "error";
    }
    public String terminateServerInstance(String instanceId)
    {
    	TerminateInstancesRequest terRequest=new TerminateInstancesRequest().withInstanceIds(instanceId);
    	ec2.terminateInstances(terRequest);
    	Set<String> instanceStatus= waitForTransitionCompletion("terminated", instanceId);
    	if(instanceStatus.contains("terminated"))
    		return "terminated";
    	else
    		return "error";
    }
    private void setInstanceNameAndOwnerByTags(List<Tag> tags,ServerInstance instance)
    {
    	for(Tag tag:tags)
    	{
    		if("Name".equals(tag.getKey()))
    			instance.setName(tag.getValue());
    		else if("owner".equals(tag.getKey()))
    			instance.setOwner(tag.getValue());
    	}
    }
    private DescribeInstancesResult filterResultWithInstanceId(String... instanceId)
    {
    	DescribeInstancesRequest request = new DescribeInstancesRequest();
        List<String> valuesT1 = new ArrayList<String>();
        valuesT1.add("subnet-1b1fce30");
        List<Filter> filterList=new ArrayList<Filter>();
        Filter filter1 = new Filter().withName("subnet-id").withValues(valuesT1);
        Filter filter2 = new Filter().withName("instance-id").withValues(instanceId);
        filterList.add(filter1);filterList.add(filter2);
        DescribeInstancesResult result = ec2.describeInstances(request.withFilters(filterList));
        return result;
    }
    private Set<String> waitForTransitionCompletion(final String desiredState,String instanceId) 
    {
    	DescribeInstancesRequest describeInstancesRequest = new DescribeInstancesRequest().withFilters(new Filter()
        .withName("subnet-id").withValues("subnet-1b1fce30"),
        new Filter().withName("instance-id").withValues(instanceId));
    	Boolean transitionCompleted = (0 == instanceId.length());
    	Set<String> instanceStates = new TreeSet<String>();
    	while (!transitionCompleted)
        {
    		instanceStates.clear();
    		try
            {
                DescribeInstancesResult describeInstancesResult = ec2.describeInstances(describeInstancesRequest);
                List<Instance> instances = extractInstances(describeInstancesResult);
                for (Instance instance : instances)
                {
                    instanceStates.add(instance.getState().getName());
                }
            }
            catch (AmazonServiceException ase)
            {
                ase.printStackTrace();
            }

            transitionCompleted = INSTANCE_STATE_COMPLETE_SET.containsAll(instanceStates);

            // Sleep until transition has completed.
            if (!transitionCompleted)
            {
            	try
            	{
            		Thread.sleep(DEFAULT_AWAIT_TRANSITION_INTERVAL);
            	}catch(InterruptedException ie)
            	{
            		ie.printStackTrace();
            	}
                
            }
        }
    	return instanceStates;
    }
    private  List<Instance> extractInstances(final DescribeInstancesResult describeInstancesResult)
    {
        List<Instance> instances = new ArrayList<Instance>();

        for (Reservation reservation : describeInstancesResult.getReservations())
        {
            instances.add(reservation.getInstances().get(0));
        }
        return instances;
    }
}
