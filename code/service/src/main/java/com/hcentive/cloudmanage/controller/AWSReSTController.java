package com.hcentive.cloudmanage.controller;

import java.util.List;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.s3.model.Bucket;
import com.hcentive.cloudmanage.service.provider.aws.EC2Service;
import com.hcentive.cloudmanage.service.provider.aws.S3Service;

@RestController
public class AWSReSTController {

	@Autowired
	private S3Service s3BucketService;
	@Autowired
	private EC2Service ec2Service;

	// **************** EC2 **********************//

	@RequestMapping(value = "/ec2/list")
	public ResponseEntity<List<Reservation>> getInstanceList() {
		List<Reservation> ec2Instances = ec2Service.getInstanceLists();
		return new ResponseEntity<List<Reservation>>(ec2Instances,
				HttpStatus.OK);
	}

	@RequestMapping(value = "/ec2/list/{instanceId}")
	public ResponseEntity<Reservation> getEC2Instance(
			@PathVariable("instanceId") String instanceId) {
		Reservation ec2Instance = ec2Service.getInstance(instanceId);
		return new ResponseEntity<Reservation>(ec2Instance, HttpStatus.OK);
	}

	@RequestMapping(value = "/ec2/start/{instanceId}")
	public ResponseEntity<String> startEC2Instance(
			@PathVariable("instanceId") String instanceId) {
		String startedInstance = ec2Service.startInstance(instanceId);
		return new ResponseEntity<String>(startedInstance, HttpStatus.OK);
	}

	@RequestMapping(value = "/ec2/stop/{instanceId}")
	public ResponseEntity<String> stopEC2Instance(
			@PathVariable("instanceId") String instanceId) {
		String stoppedInstance = ec2Service.stopInstance(instanceId);
		return new ResponseEntity<String>(stoppedInstance, HttpStatus.OK);
	}

	// **************** Quartz **********************//

	@RequestMapping(value = "/ec2/schedule/listJobs")
	public ResponseEntity<Set<JobKey>> scheduledEC2InstanceJobs() {
		ResponseEntity<Set<JobKey>> responseEntity;
		Set<JobKey> scheduled;
		try {
			scheduled = ec2Service.listScheduledInstanceJobs();
			responseEntity = new ResponseEntity<Set<JobKey>>(scheduled,
					HttpStatus.OK);
		} catch (SchedulerException e) {
			responseEntity = new ResponseEntity<Set<JobKey>>(
					HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return responseEntity;
	}

	@RequestMapping(value = "/ec2/schedule/listTriggers")
	public ResponseEntity<Set<TriggerKey>> scheduledEC2InstanceTriggers() {
		ResponseEntity<Set<TriggerKey>> responseEntity;
		Set<TriggerKey> scheduled = null;
		try {
			scheduled = ec2Service.listScheduledInstanceTriggers();
			responseEntity = new ResponseEntity<Set<TriggerKey>>(scheduled,
					HttpStatus.OK);
		} catch (SchedulerException e) {
			responseEntity = new ResponseEntity<Set<TriggerKey>>(
					HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return responseEntity;
	}

	@RequestMapping(value = "ec2/schedule/deleteJob")
	public ResponseEntity<String> deleteJob(@RequestParam String jobGroup,
			@RequestParam String jobName) {
		ResponseEntity<String> response = null;
		try {
			if (ec2Service.deleteJob(jobGroup, jobName)) {
				response = new ResponseEntity<String>(
						"Successfully deleted Job", HttpStatus.OK);
			}
		} catch (SchedulerException e) {
			response = new ResponseEntity<String>("Failed to delete Job",
					HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	@RequestMapping(value = "ec2/schedule/deleteTrigger")
	public ResponseEntity<String> deleteTrigger(
			@RequestParam String triggerGroup, @RequestParam String triggerName) {
		ResponseEntity<String> response = null;
		try {
			if (ec2Service.deleteTrigger(triggerGroup, triggerName)) {
				response = new ResponseEntity<String>(
						"Successfully deleted Trigger", HttpStatus.OK);
			}
		} catch (SchedulerException e) {
			response = new ResponseEntity<String>("Failed to delete Trigger",
					HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// Create Job
	@RequestMapping(value = "ec2/schedule/createJob")
	public ResponseEntity<JobDetail> createJob(@RequestParam String jobGroup,
			@RequestParam String jobName, @RequestParam String jobType) {
		ResponseEntity<JobDetail> response = null;
		try {
			JobDetail jobDetail = ec2Service.createJob(jobGroup, jobName,
					jobType);
			response = new ResponseEntity<JobDetail>(jobDetail, HttpStatus.OK);
		} catch (SchedulerException e) {
			response = new ResponseEntity<JobDetail>(
					HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// Update Trigger.
	@RequestMapping(value = "/ec2/schedule/update")
	public ResponseEntity<String> updateTrigger(
			@RequestParam String triggerGroup,
			@RequestParam String triggerName,
			@RequestParam String cronExpression) {
		ResponseEntity<String> response = new ResponseEntity<String>(
				"Updated Trigger", HttpStatus.OK);
		try {
			ec2Service.updateTrigger(triggerGroup, triggerName, cronExpression);
		} catch (SchedulerException e) {
			response = new ResponseEntity<String>("Failed to update Trigger",
					HttpStatus.INTERNAL_SERVER_ERROR);
			e.printStackTrace();
		}
		return response;
	}

	// Schedule Job and Trigger
	@RequestMapping(value = "/ec2/schedule/")
	public ResponseEntity<String> schedule(@RequestParam String jobGroup,
			@RequestParam String jobName, @RequestParam String triggerGroup,
			@RequestParam String triggerName,
			@RequestParam String cronExpression) {
		String response = ec2Service.scheduleInstance(jobGroup, jobName,
				triggerGroup, triggerName, cronExpression);
		return new ResponseEntity<String>(response, HttpStatus.OK);
	}

	// **************** S3 **********************//

	@RequestMapping(value = "/s3/list")
	public ResponseEntity<List<Bucket>> getBucketList() {
		List<Bucket> buckets = s3BucketService.getBucketLists();
		return new ResponseEntity<List<Bucket>>(buckets, HttpStatus.OK);
	}

	@RequestMapping(value = "/s3/list/{bucketName}")
	public ResponseEntity<List<String>> getBucketList(
			@PathVariable("bucketName") String bucketName) {
		List<String> contents = null;
		if (bucketName != null) {
			contents = s3BucketService.getBucketList(bucketName);
		}
		return new ResponseEntity<List<String>>(contents, HttpStatus.OK);
	}

}
