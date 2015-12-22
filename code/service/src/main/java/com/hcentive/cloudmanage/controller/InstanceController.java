package com.hcentive.cloudmanage.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.JobTriggerInfo;
import com.hcentive.cloudmanage.domain.JobTriggerInfoDTO;
import com.hcentive.cloudmanage.domain.StartJobTriggerInfo;
import com.hcentive.cloudmanage.domain.StopJobTriggerInfo;
import com.hcentive.cloudmanage.job.InstanceJobDetails;
import com.hcentive.cloudmanage.service.provider.aws.AWSUtils;
import com.hcentive.cloudmanage.service.provider.aws.EC2Service;

@RestController
@RequestMapping("/instances")
public class InstanceController {
	
	@Autowired
	private EC2Service ec2Service;

	
	@RequestMapping(method=RequestMethod.GET)
	public List<Instance> list(@RequestParam(value="group",required=false) List<String> groups) throws AccessDeniedException{
		List<Instance> ec2Instances = ec2Service.getInstanceLists();
		return ec2Instances;
	}
	
	@RequestMapping(value="/{instanceID}",method=RequestMethod.PUT, params={"action=stop"})
	public StopInstancesResult stopInstance(@PathVariable(value="instanceID") String instanceID){
		StopInstancesResult stoppedInstance = ec2Service.stopInstance(instanceID);
		return stoppedInstance;
	}
	
	@RequestMapping(value="/{instanceID}",method=RequestMethod.PUT, params={"action=start"})
	public StartInstancesResult startInstance(@PathVariable(value="instanceID") String instanceID){
		StartInstancesResult startedInstance = ec2Service.startInstance(instanceID);
		return startedInstance;
	}
	
	@RequestMapping(value="/{instanceID}",method=RequestMethod.PUT, params={"action=terminate"})
	public TerminateInstancesResult terminateInstance(@PathVariable(value="instanceID") String instanceID){
		TerminateInstancesResult terminatedIntance = ec2Service.terminateInstance(instanceID);
		return terminatedIntance;
		
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

}
