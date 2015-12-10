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

import com.hcentive.cloudmanage.domain.Instance;
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
	public String stopInstance(@PathVariable(value="instanceID") String instanceID){
		String stoppedInstance = ec2Service.stopInstance(instanceID);
		return stoppedInstance;
	}
	
	@RequestMapping(value="/{instanceID}",method=RequestMethod.PUT, params={"action=start"})
	public String startInstance(@PathVariable(value="instanceID") String instanceID){
		String startedInstance = ec2Service.startInstance(instanceID);
		return startedInstance;
	}
	
	@RequestMapping(value="/{instanceID}",method=RequestMethod.PUT, params={"action=terminate"})
	public String terminateInstance(@PathVariable(value="instanceID") String instanceID){
		String terminatedIntance = ec2Service.terminateInstance(instanceID);
		return terminatedIntance;
		
	}
	
	@RequestMapping(value="/schedule", method=RequestMethod.POST)
	public String scheduleInstance(@RequestParam(value="costCenter") String costCenter, @RequestParam(value="instanceName") String instanceName, 
			  @RequestParam(value="startCron") String startCronExpression, 
			 @RequestParam(value="stopCron") String stopCronExpression) throws SchedulerException{
		String startResponse = scheduleStartInstance(costCenter, instanceName, startCronExpression);
		if(startResponse.equals("Success!")){
			String stopResponse = scheduleStopInstance(costCenter, instanceName, stopCronExpression);
			return stopResponse;
		}
		return startResponse;
		
	}

	private String scheduleStartInstance(String costCenter, String instanceName,
			String startCronExpression) throws SchedulerException {
		return abstractScheduleInstance(costCenter,instanceName, startCronExpression, AWSUtils.START_INSTANCE_JOB_TYPE);
		
	}

	private String scheduleStopInstance(String costCenter, String instanceName,
			String stopCronExpression) throws SchedulerException {
		return abstractScheduleInstance(costCenter,instanceName, stopCronExpression, AWSUtils.STOP_INSTANCE_JOB_TYPE);
		
		
	}
	
	
	

	private String abstractScheduleInstance(String costCenter, String instanceName,
			String cronExpression, String type) throws SchedulerException {
		String jobGroup = costCenter + "_job";
		String triggerGroup = costCenter + "_trigger";
		String jobName = instanceName + "_"+type;
		String jobType = type;
		String triggerName = jobName + "_trigger";
		ec2Service.createJob(jobGroup, jobName, jobType);
		return ec2Service.scheduleInstance(jobGroup, jobName, triggerGroup, triggerName, cronExpression);
		
	}

}
