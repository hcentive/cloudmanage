package com.hcentive.cloudmanage.service.provider.aws;

import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.regions.ServiceAbbreviations;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.hcentive.cloudmanage.job.DynamicJobScheduler;
import com.hcentive.cloudmanage.domain.AWSClientProxy;

@Service("ec2Service")
public class EC2ServiceImpl implements EC2Service {

	private static final Logger logger = LoggerFactory
			.getLogger(EC2ServiceImpl.class.getName());

	@Autowired
	private AWSClientProxy awsClientProxy;

	public AmazonEC2Client getEC2Session() {
		return (AmazonEC2Client) awsClientProxy
				.getClient(ServiceAbbreviations.EC2);
	}

	/**
	 * Lists AWS bucket.
	 */
	public Reservation getInstance(String instanceId) {
		logger.info("Instance info");
		DescribeInstancesRequest request = new DescribeInstancesRequest()
				.withInstanceIds(instanceId);
		DescribeInstancesResult instances = getEC2Session().describeInstances(
				request);
		Reservation instance = instances.getReservations().get(0);
		logger.debug("Instance info " + instance);
		return instance;
	}

	/**
	 * Lists EC2 Instances.
	 */
	public List<Reservation> getInstanceLists() {
		logger.info("Listing instances");
		DescribeInstancesResult instances = getEC2Session().describeInstances();
		List<Reservation> reservations = instances.getReservations();
		for (Reservation instance : reservations) {
			logger.debug(" - " + instance.toString());
		}
		return reservations;
	}

	/**
	 * Stops an EC2 instance.
	 */
	public String stopInstance(String instanceId) {
		logger.info("Stopping instance " + instanceId);
		// Check if the rootDeviceType is 'ebs' or 'instance store'.
		StopInstancesRequest stopRequest = new StopInstancesRequest()
				.withInstanceIds(instanceId);
		StopInstancesResult stoppedInstances = getEC2Session().stopInstances(
				stopRequest);
		logger.debug("Instance stopped " + stoppedInstances);
		return stoppedInstances.toString();
	}

	/**
	 * Starts an EC2 instance.
	 */
	public String startInstance(String instanceId) {
		logger.info("Starting instance " + instanceId);
		// Check if the rootDeviceType is 'ebs' or 'instance store'.
		StartInstancesRequest staetRequest = new StartInstancesRequest()
				.withInstanceIds(instanceId);
		StartInstancesResult startedInstances = getEC2Session().startInstances(
				staetRequest);
		logger.debug("Instance started " + startedInstances);
		return startedInstances.toString();
	}

	// ****************** QUARTZ ***********************//

	@Autowired
	private DynamicJobScheduler scheduler;

	public String scheduleInstance(String jobGroup, String jobName,
			String triggerGroup, String triggerName, String cronExpression) {
		String response = "Failed!";
		try {
			scheduler.schedule(jobGroup, jobName, triggerGroup, triggerName,
					cronExpression);
			response = "Success!";
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return response;
	}

	/**
	 * Creates a Un-Schedule Job.
	 */
	public JobDetail createJob(String jobGroup, String jobName, String jobType)
			throws SchedulerException {
		return scheduler.createJob(jobGroup, jobName, jobType);
	}

	/**
	 * Lists all [un]scheduled Jobs.
	 */
	public Set<JobKey> listScheduledInstanceJobs() throws SchedulerException {
		return scheduler.listScheduledInstanceJobs();
	}

	/**
	 * Lists all [un]attached Jobs.
	 */
	public Set<TriggerKey> listScheduledInstanceTriggers()
			throws SchedulerException {
		return scheduler.listScheduledInstanceTriggers();
	}

	/**
	 * Update the trigger.
	 */
	public void updateTrigger(String triggerGroup, String triggerName,
			String cronExpression) throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroup);
		Trigger newTrigger = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				// This might not be required. Still!
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.build();
		scheduler.updateTrigger(triggerKey, newTrigger);
	}

	/**
	 * Remove the Job and all associated triggers.
	 */
	public boolean deleteJob(String jobGroup, String jobName)
			throws SchedulerException {
		return scheduler.deleteJob(jobGroup, jobName);
	}

	/**
	 * Remove the trigger and unschedule the job.
	 */
	public boolean deleteTrigger(String triggerGroup, String triggerName)
			throws SchedulerException {
		return scheduler.deleteTrigger(triggerGroup, triggerName);
	}

}
