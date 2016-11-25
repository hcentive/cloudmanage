package com.hcentive.cloudmanage.controller;

import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.ec2.model.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.hcentive.cloudmanage.billing.AWSMetaInfo;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.JobTriggerInfo;
import com.hcentive.cloudmanage.domain.JobTriggerInfoDTO;
import com.hcentive.cloudmanage.job.InstanceJobDetails;
import com.hcentive.cloudmanage.profiling.CPUThresholdInfo;
import com.hcentive.cloudmanage.profiling.DatapointMixin;
import com.hcentive.cloudmanage.profiling.ProfileInfo;
import com.hcentive.cloudmanage.service.provider.aws.AWSUtils;
import com.hcentive.cloudmanage.service.provider.aws.CloudWatchService;
import com.hcentive.cloudmanage.service.provider.aws.EC2Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Api(basePath = "/instances",
		value = "instances",
		description = "Api endpoint related to EC2 instances",
		produces = "application/json",
		tags = "instance")
@RestController
@RequestMapping("/instances")
public class InstanceController {

	@Autowired
	private EC2Service ec2Service;

	@Autowired
	private CloudWatchService cloudWatchService;

	private static final Logger logger = LoggerFactory
			.getLogger(InstanceController.class.getName());

	@ApiOperation(value = "Get list of all ec2 instances",nickname = "Get list of all ec2 instances")
	@RequestMapping(method = RequestMethod.GET)
	public List<Instance> list() throws AccessDeniedException {
		List<Instance> ec2Instances = ec2Service.getInstanceLists(false);
		return ec2Instances;
	}

	@ApiOperation(value = "Get detail information of all ec2 instances",nickname = "Get detail information of all ec2 instances")
	@RequestMapping(value = "/meta", method = RequestMethod.GET)
	public List<AWSMetaInfo> getInstanceMetaDataList() {
		List<AWSMetaInfo> awsMetaInfoList = ec2Service.getAWSMetaInfoList();
		return awsMetaInfoList;
	}

	@ApiOperation(value = "Get instance details by ip address",nickname = "Get instance details by ip address")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "privateIPAddress",value = "Private IP Address",
					required = true,dataType = "string",paramType = "path")
	})
	@RequestMapping(value = "/ip/{privateIPAddress:.+}",method = RequestMethod.GET)
	public Instance getInstanceByIPAddress(
			@PathVariable(value = "privateIPAddress") String privateIPAddress) {
		Instance instance = ec2Service.getInstanceByPrivateIP(privateIPAddress);
		return instance;
	}

//	@ApiOperation(value = "Stop ec2 instance",nickname = "Stop ec2 instance")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "instanceID",value = "Instance id",required = true,dataType = "string",paramType = "path")
//	})
	@ApiIgnore
	@RequestMapping(value = "/{instanceID}", method = RequestMethod.PUT, params = { "action=stop" })
	public InstanceState stopInstance(
			@PathVariable(value = "instanceID") String instanceID) {
		StopInstancesResult stoppedInstance = ec2Service.stopInstance(
				instanceID, false);
		InstanceState finalStatus = null;
		if (stoppedInstance != null) {
			InstanceState currentStatus = getCurrentState(stoppedInstance
					.getStoppingInstances());
			finalStatus = pollInstance(currentStatus, instanceID);
		} else {
			// get initial state back
			finalStatus = ec2Service.getInstance(instanceID).getAwsInstance()
					.getState();
		}
		return finalStatus;
	}

//	@ApiOperation(value = "Start ec2 instance",nickname = "Start ec2 instance")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "instanceID",value = "Instance id",required = true,dataType = "string",paramType = "path")
//	})
	@ApiIgnore
	@RequestMapping(value = "/{instanceID}", method = RequestMethod.PUT, params = { "action=start" })
	public InstanceState startInstance(
			@PathVariable(value = "instanceID") String instanceID) {
		StartInstancesResult startedInstance = ec2Service
				.startInstance(instanceID);
		InstanceState currentStatus = getCurrentState(startedInstance
				.getStartingInstances());
		InstanceState finalStatus = pollInstance(currentStatus, instanceID);
		return finalStatus;
	}

//	@ApiOperation(value = "Terminate ec2 instance",nickname = "Terminate ec2 instance")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "instanceID",value = "Instance id",required = true,dataType = "string",paramType = "path")
//	})
	@ApiIgnore
	@RequestMapping(value = "/{instanceID}", method = RequestMethod.PUT, params = { "action=terminate" })
	public InstanceState terminateInstance(
			@PathVariable(value = "instanceID") String instanceID) {
		TerminateInstancesResult terminatedIntance = ec2Service
				.terminateInstance(instanceID);
		InstanceState currentStatus = getCurrentState(terminatedIntance
				.getTerminatingInstances());
		InstanceState finalStatus = pollInstance(currentStatus, instanceID);
		return finalStatus;

	}

	@ApiOperation(value = "Get schedule of ec2 instance",nickname = "Get schedule of ec2 instance")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "instanceID",value = "Instance id",required = true,dataType = "string",paramType = "path")
	})
	@RequestMapping(value = "/schedule/{instanceID}", method = RequestMethod.GET)
	public InstanceJobDetails getSchedule(
			@PathVariable(value = "instanceID") String instanceId)
			throws SchedulerException {
		InstanceJobDetails jobDetails = ec2Service
				.getInstanceJobDetails(instanceId);
		return jobDetails;
	}

//	@ApiOperation(value = "Create schedule of ec2 instance",nickname = "Create schedule of ec2 instance")
	@ApiIgnore
	@RequestMapping(value = "/schedule/{instanceID}", method = RequestMethod.POST)
	public void scheduleInstance(JobTriggerInfoDTO jobTriggerInfoDTO,
			@PathVariable(value = "instanceID") String instanceId)
			throws SchedulerException {
		ec2Service.scheduleInstance(jobTriggerInfoDTO.getStartJobTriggerInfo(),
				jobTriggerInfoDTO.getStopJobTriggerInfo(), instanceId);
	}

//	@ApiOperation(value = "Update schedule of ec2 instance",nickname = "Update schedule of ec2 instance")
	@ApiIgnore
	@RequestMapping(value = "/schedule/update/{instanceID}", method = RequestMethod.POST)
	public void updateScheduleInstance(
			@PathVariable(value = "instanceID") String instanceId,
			JobTriggerInfoDTO jobTriggerInfoDTO) throws SchedulerException {
		ec2Service.updateTrigger(jobTriggerInfoDTO.getStartJobTriggerInfo(),
				jobTriggerInfoDTO.getStopJobTriggerInfo());
	}

//	@ApiOperation(value = "Delete schedule of ec2 instance",nickname = "Delete schedule of ec2 instance")
//	@ApiImplicitParams({
//			@ApiImplicitParam(name = "instanceID",value = "Instance id",required = true,dataType = "string",paramType = "path"),
//			@ApiImplicitParam(name = "costCenter",value = "Cost Center",required = true,dataType = "string",paramType = "query")
//	})
	@ApiIgnore
	@RequestMapping(value = "/schedule/delete/{instanceID}", method = RequestMethod.POST)
	public void deleteScheduleInstance(
			@RequestParam(value = "costCenter") String costCenter,
			@PathVariable(value = "instanceID") String instanceId)
			throws SchedulerException {
		JobTriggerInfo startJobTriggerInfo = new JobTriggerInfo(costCenter,
				instanceId, AWSUtils.START_INSTANCE_JOB_TYPE);
		JobTriggerInfo stopJobTriggerInfo = new JobTriggerInfo(costCenter,
				instanceId, AWSUtils.STOP_INSTANCE_JOB_TYPE);
		ec2Service.deleteJob(startJobTriggerInfo, stopJobTriggerInfo,
				instanceId);
	}

	@ApiOperation(value = "Get CPU Utilization of ec2 instance",nickname = "Get CPU Utilization of ec2 instance")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "instanceID",value = "Instance id",required = true,dataType = "string",paramType = "path"),
			@ApiImplicitParam(name = "fromDate",value = "mm/dd/yyyy",required = true,dataType = "string",paramType = "query"),
			@ApiImplicitParam(name = "toDate",value = "mm/dd/yyyy",dataType = "string",paramType = "query"),
			@ApiImplicitParam(name = "period",value = "Period",dataType = "int",paramType = "query")
	})
	@RequestMapping(value = "/cpu/{instanceID}", method = RequestMethod.GET)
	public List<Datapoint> getCPUUtilizationData(
			@PathVariable(value = "instanceID") String instanceId,
			@RequestParam(value = "fromDate") Date fromDate,
			@RequestParam(value = "toDate", required = false) Date toDate,
			@RequestParam(value = "period", required = false) Integer period) {
		if (toDate == null) {
			toDate = new Date();
		}
		if (period == null) {
			period = 60 * 60;
		}
		List<Datapoint> response = new ArrayList<Datapoint>();
		List<ProfileInfo> profileInfo = cloudWatchService.getMetrics(
				instanceId, fromDate, toDate);
		if (!CollectionUtils.isEmpty(profileInfo)) {
			ObjectMapper mapper = new ObjectMapper();
			mapper.addMixInAnnotations(Datapoint.class, DatapointMixin.class);

			try {
				for (ProfileInfo _profileInfo : profileInfo) {
					response.addAll(mapper.readValue(
							_profileInfo.getAvgCPUHourly(),
							TypeFactory.defaultInstance()
									.constructCollectionType(List.class,
											Datapoint.class)));
				}

			} catch (IOException e) {
				logger.error(
						"Failed to parse json avg cpu utilization back to Datapoints for instance {} on {} with {}",
						instanceId, fromDate, e);
			}
		}
		return response;
	}

	private InstanceState pollInstance(InstanceState initialStatus,
			String instanceId) {
		InstanceState newStatus = initialStatus;
		while (newStatus.equals(initialStatus)) {
			Instance instance = ec2Service.getInstance(instanceId);
			newStatus = instance.getAwsInstance().getState();
		}
		return newStatus;
	}

	private InstanceState getCurrentState(List<InstanceStateChange> instances) {
		return instances.get(0).getCurrentState();
	}

	@ApiOperation(value = "Get CPU Threshold of ec2 instance",nickname = "Get CPU Threshold of ec2 instance")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "instanceID",value = "Instance id",required = true,dataType = "string",paramType = "path")
	})
	@RequestMapping(value = "/threshold/{instanceID}", method = RequestMethod.GET)
	public CPUThresholdInfo getCPUThreshold(
			@PathVariable(value = "instanceID") String instanceId) {
		CPUThresholdInfo cpuThresholdInfo = cloudWatchService
				.getCPUThreshold(instanceId);
		logger.debug("Returning {}", cpuThresholdInfo);
		return cpuThresholdInfo;
	}

//	@ApiOperation(value = "Create CPU Threshold of ec2 instance",nickname = "Create CPU Threshold of ec2 instance")
	@ApiIgnore
	@RequestMapping(value = "/threshold", method = RequestMethod.POST)
	public void setCPUThreshold(
			@ModelAttribute("cpuThresholdInfo") CPUThresholdInfo cpuThresholdInfo) {
		logger.debug("Perisiting {}", cpuThresholdInfo);
		cloudWatchService.setCPUThreshold(cpuThresholdInfo);
	}
}
