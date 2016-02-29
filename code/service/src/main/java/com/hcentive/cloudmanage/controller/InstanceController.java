package com.hcentive.cloudmanage.controller;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStateChange;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.hcentive.cloudmanage.billing.AWSMetaInfo;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.JobTriggerInfo;
import com.hcentive.cloudmanage.domain.JobTriggerInfoDTO;
import com.hcentive.cloudmanage.job.InstanceJobDetails;
import com.hcentive.cloudmanage.profiling.ProfileInfo;
import com.hcentive.cloudmanage.service.provider.aws.AWSUtils;
import com.hcentive.cloudmanage.service.provider.aws.CloudWatchService;
import com.hcentive.cloudmanage.service.provider.aws.EC2Service;

@RestController
@RequestMapping("/instances")
public class InstanceController {
	
	@Autowired
	private EC2Service ec2Service;
	
	@Autowired
	private CloudWatchService cloudWatchService;

	private static final Logger logger = LoggerFactory
			.getLogger(InstanceController.class.getName());

	@RequestMapping(method=RequestMethod.GET)
	public List<Instance> list() throws AccessDeniedException{
		List<Instance> ec2Instances = ec2Service.getInstanceLists(false);
		return ec2Instances;
	}
	
	@RequestMapping(value="/meta", method=RequestMethod.GET)
	public List<AWSMetaInfo> getInstanceMetaDataList(){
		List<AWSMetaInfo> awsMetaInfoList = ec2Service.getAWSMetaInfoList();
		return awsMetaInfoList;
		
	}
	
	@RequestMapping(value="/ip/{privateIPAddress:.+}")
	public Instance getInstanceByIPAddress(@PathVariable(value="privateIPAddress") String privateIPAddress){
		Instance instance = ec2Service.getInstanceByPrivateIP(privateIPAddress);
		return instance;
	}
	
	@RequestMapping(value="/{instanceID}",method=RequestMethod.PUT, params={"action=stop"})
	public InstanceState stopInstance(@PathVariable(value="instanceID") String instanceID){
		StopInstancesResult stoppedInstance = ec2Service.stopInstance(instanceID);
		InstanceState currentStatus = getCurrentState(stoppedInstance.getStoppingInstances());
		InstanceState finalStatus = pollInstance(currentStatus, instanceID);
		return finalStatus;
	}
	
	@RequestMapping(value="/{instanceID}",method=RequestMethod.PUT, params={"action=start"})
	public InstanceState startInstance(@PathVariable(value="instanceID") String instanceID){
		StartInstancesResult startedInstance = ec2Service.startInstance(instanceID);
		InstanceState currentStatus = getCurrentState(startedInstance.getStartingInstances());
		InstanceState finalStatus = pollInstance(currentStatus, instanceID);
		return finalStatus;
	}
	
	@RequestMapping(value="/{instanceID}",method=RequestMethod.PUT, params={"action=terminate"})
	public InstanceState terminateInstance(@PathVariable(value="instanceID") String instanceID){
		TerminateInstancesResult terminatedIntance = ec2Service.terminateInstance(instanceID);
		InstanceState currentStatus = getCurrentState(terminatedIntance.getTerminatingInstances());
		InstanceState finalStatus = pollInstance(currentStatus, instanceID);
		return finalStatus;
		
	}
	
	@RequestMapping(value="/schedule/{instanceID}", method=RequestMethod.GET)
	public InstanceJobDetails getSchedule(@PathVariable(value="instanceID") String instanceId) throws SchedulerException{
		InstanceJobDetails jobDetails = ec2Service.getInstanceJobDetails(instanceId);
		return jobDetails;
	}
	
	@RequestMapping(value="/schedule/{instanceID}", method=RequestMethod.POST)
	public void scheduleInstance(JobTriggerInfoDTO jobTriggerInfoDTO, 
			 @PathVariable(value="instanceID") String instanceId) throws SchedulerException{
		ec2Service.scheduleInstance(jobTriggerInfoDTO.getStartJobTriggerInfo(), jobTriggerInfoDTO.getStopJobTriggerInfo(), instanceId);
	}
	
	
	@RequestMapping(value="/schedule/update/{instanceID}", method=RequestMethod.POST)
	public void updateScheduleInstance(@PathVariable(value="instanceID") String instanceId, 
			JobTriggerInfoDTO jobTriggerInfoDTO) throws SchedulerException{
		ec2Service.updateTrigger(jobTriggerInfoDTO.getStartJobTriggerInfo(), jobTriggerInfoDTO.getStopJobTriggerInfo());
	}
	
	@RequestMapping(value="/schedule/delete/{instanceID}", method=RequestMethod.POST)
	public void deleteScheduleInstance(@RequestParam(value="costCenter") String costCenter, @PathVariable(value="instanceID") String instanceId) throws SchedulerException{
		JobTriggerInfo startJobTriggerInfo = new JobTriggerInfo(costCenter, instanceId, AWSUtils.START_INSTANCE_JOB_TYPE);
		JobTriggerInfo stopJobTriggerInfo = new JobTriggerInfo(costCenter, instanceId, AWSUtils.STOP_INSTANCE_JOB_TYPE);
		ec2Service.deleteJob(startJobTriggerInfo, stopJobTriggerInfo, instanceId);
	}
	
	@RequestMapping(value="/cpu/{instanceID}", method=RequestMethod.GET)
	public List<Datapoint> getCPUUtilizationData(@PathVariable(value="instanceID") String instanceId, @RequestParam(value="fromDate") Date fromDate, 
			@RequestParam(value="toDate", required=false) Date toDate, @RequestParam(value="period", required=false) Integer period){
		if(toDate == null){
			toDate = new Date();
		}
		if(period == null){
			period = 60*60;
		}
		List<ProfileInfo> profileInfo = cloudWatchService.getMetrics(
				instanceId, fromDate, toDate);
		ObjectMapper mapper = new ObjectMapper();
		List<Datapoint> response = new ArrayList<Datapoint>();
		try {
			response.add(mapper.readValue(
					// TODO: assuming just 1 day and response else loop
					profileInfo.get(0).getAvgCPUHourly(),
					TypeFactory.defaultInstance().constructCollectionType(
							List.class, Datapoint.class)));
		} catch (IOException e) {
			logger.error(
					"Failed to parse json avg cpu utilization back to Datapoints for instance {} on {}",
					instanceId, fromDate);
		}
		return response;
	}
	
	private InstanceState pollInstance(InstanceState initialStatus, String instanceId){
		InstanceState newStatus = initialStatus;
		while(newStatus.equals(initialStatus)){
			Instance instance = ec2Service.getInstance(instanceId);
			newStatus = instance.getAwsInstance().getState();
		}	
		return newStatus;
	}
	
	private InstanceState getCurrentState(List<InstanceStateChange> instances){
		return instances.get(0).getCurrentState();
	}
	

}
