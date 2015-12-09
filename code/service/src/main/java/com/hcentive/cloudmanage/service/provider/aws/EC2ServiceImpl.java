package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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
import com.hcentive.cloudmanage.domain.AWSClientProxy;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.job.DynamicJobScheduler;
import com.hcentive.cloudmanage.security.DecisionMapper;
import com.hcentive.cloudmanage.security.DecisionMapperRepository;

@Service("ec2Service")
public class EC2ServiceImpl implements EC2Service {

	private static final Logger logger = LoggerFactory
			.getLogger(EC2ServiceImpl.class.getName());

	@Autowired
	private AWSClientProxy awsClientProxy;

	@Autowired
	private DecisionMapperRepository decisionMapperRepository;

	/**
	 * A session must be requested for each call because it has a role
	 * associated with it.
	 */
	public AmazonEC2Client getEC2Session(boolean applyPolicy) {
		Map<String, String> decisionMapAsPolicy = null;
		if (applyPolicy) {
			decisionMapAsPolicy = getDecisionMapAsPolicy();
		}
		return awsClientProxy.getEC2Client(applyPolicy, decisionMapAsPolicy);
	}

	/**
	 * Utility to translate the Decision Map for Policy to start-stop AWS
	 * instances.
	 */
	private Map<String, String> getDecisionMapAsPolicy() {

		Map<String, String> accessCond = new HashMap<>();

		for (DecisionMapper decisionMap : getDecisionMap()) {
			String tagType = decisionMap.getTag().getTagType();
			String tagValue = decisionMap.getTag().getTagValue();
			accessCond.put("ec2:ResourceTag/" + tagType, tagValue);
		}

		logger.info("Policy Conditions available " + accessCond);
		return accessCond;
	}

	/**
	 * Utility to translate the Decision Map for Filters to list AWS instances.
	 */
	private List<Filter> getDecisionMapAsFilters() {

		List<Filter> filters = new ArrayList<Filter>();

		for (DecisionMapper decisionMap : getDecisionMap()) {
			String tagType = decisionMap.getTag().getTagType();
			String tagValue = decisionMap.getTag().getTagValue();
			// found same filter - add the Value
			boolean modified = false;
			for (Filter f : filters) {
				if (f.getName().equalsIgnoreCase("tag:" + tagType)) {
					f.getValues().add(tagValue);
					modified = true;
				}
			}
			// else - add the Filter.
			if (!modified) {
				filters.add(new Filter("tag:" + tagType).withValues(tagValue));
			}
		}

		logger.info("Filters available " + filters);
		return filters;
	}

	/**
	 * Retrieve Mapping from DB for Authority.
	 * 
	 * - DecisionMapper [tag=Tag [tagType=stack, tagValue=qa],
	 * ldapAuthNames=techops,Ops,qa-*]
	 */
	private Set<DecisionMapper> getDecisionMap() {
		Set<DecisionMapper> decisionMapper = new HashSet<>();
		// Dummy for the time being.
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder
				.getContext().getAuthentication().getAuthorities();
		for (GrantedAuthority auth : authorities) {
			decisionMapper.addAll(decisionMapperRepository.findByRole(auth
					.getAuthority()));
		}
		logger.info("Mappers available " + decisionMapper);
		return decisionMapper;
	}

	/**
	 * Lists AWS bucket.
	 */
	public Reservation getInstance(String instanceId) {
		logger.info("Instance info");
		Reservation reservation = null;
		DescribeInstancesRequest request = new DescribeInstancesRequest()
				.withInstanceIds(instanceId).withFilters(
						getDecisionMapAsFilters());
		DescribeInstancesResult instanceResult = getEC2Session(false)
				.describeInstances(request);
		List<Reservation> reservations = instanceResult.getReservations();
		if (!reservations.isEmpty()) {
			reservation = reservations.get(0);
		}
		logger.debug("Instance info " + reservation);
		return reservation;
	}

	/**
	 * Lists EC2 Instances.
	 */
	public List<Instance> getInstanceLists() {
		logger.info("Listing instances");
		DescribeInstancesRequest instanceRequest = new DescribeInstancesRequest()
				.withFilters(getDecisionMapAsFilters());
		DescribeInstancesResult instancesResult = getEC2Session(false)
				.describeInstances(instanceRequest);
		List<Instance> instances = AWSUtils.extractInstances(instancesResult);
		return instances;
	}

	/**
	 * Stops an EC2 instance.
	 */
	public String stopInstance(String instanceId) {
		logger.info("Stopping instance " + instanceId);
		// Check if the rootDeviceType is 'ebs' or 'instance store'.
		StopInstancesRequest stopRequest = new StopInstancesRequest()
				.withInstanceIds(instanceId);
		StopInstancesResult stoppedInstances = getEC2Session(true)
				.stopInstances(stopRequest);
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
		StartInstancesResult startedInstances = getEC2Session(true)
				.startInstances(staetRequest);
		logger.debug("Instance started " + startedInstances);
		return startedInstances.toString();
	}

	public String terminateInstance(String instanceId) {
		TerminateInstancesRequest request = new TerminateInstancesRequest();
		request.withInstanceIds(instanceId);
		TerminateInstancesResult result = getEC2Session(true)
				.terminateInstances(request);
		return result.toString();
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
