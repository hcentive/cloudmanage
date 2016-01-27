package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
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
import com.amazonaws.services.ec2.model.DescribeInstanceStatusRequest;
import com.amazonaws.services.ec2.model.DescribeInstanceStatusResult;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.InstanceStatus;
import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.hcentive.cloudmanage.audit.Auditable;
import com.hcentive.cloudmanage.audit.Auditable.AuditingEventType;
import com.hcentive.cloudmanage.domain.AWSClientProxy;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.JobTriggerInfo;
import com.hcentive.cloudmanage.job.DynamicJobScheduler;
import com.hcentive.cloudmanage.job.InstanceJobDetails;
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

			// If key exists add a comma separated value.
			String key = "ec2:ResourceTag/" + tagType;
			if (accessCond.get(key) != null) {
				tagValue = accessCond.get(key) + "," + tagValue;
			}
			accessCond.put(key, tagValue);
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
	public Instance getInstance(String instanceId) {
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
		Instance instance = AWSUtils.extractInstance(reservation);
		return instance;
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
	@Auditable(AuditingEventType.EC2_STOP)
	public StopInstancesResult stopInstance(String instanceId) {
		logger.info("Stopping instance " + instanceId);
		// Check if the rootDeviceType is 'ebs' or 'instance store'.
		StopInstancesRequest stopRequest = new StopInstancesRequest()
				.withInstanceIds(instanceId);
		StopInstancesResult stoppedInstances = getEC2Session(SecurityContextHolder.getContext().getAuthentication() != null)
				.stopInstances(stopRequest);
		logger.debug("Instance stopped " + stoppedInstances);
		return stoppedInstances;
	}

	/**
	 * Starts an EC2 instance.
	 */
	@Auditable(AuditingEventType.EC2_START)
	public StartInstancesResult startInstance(String instanceId) {
		logger.info("Starting instance " + instanceId);
		// Check if the rootDeviceType is 'ebs' or 'instance store'.
		StartInstancesRequest startRequest = new StartInstancesRequest()
				.withInstanceIds(instanceId);
		StartInstancesResult startedInstances = getEC2Session(SecurityContextHolder.getContext().getAuthentication() != null)
				.startInstances(startRequest);
		logger.debug("Instance started " + startedInstances);
		return startedInstances;
	}

	public TerminateInstancesResult terminateInstance(String instanceId) {
		TerminateInstancesRequest request = new TerminateInstancesRequest();
		request.withInstanceIds(instanceId);
		TerminateInstancesResult result = getEC2Session(true)
				.terminateInstances(request);
		return result;
	}

	// ****************** QUARTZ ***********************//

	@Autowired
	private DynamicJobScheduler scheduler;

	
	public InstanceJobDetails getInstanceJobDetails(String instanceId) throws SchedulerException{
		return scheduler.getInstanceJobDetails(instanceId);
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
	@Auditable(AuditingEventType.EC2_SCHEDULE_UPDATED)
	public void updateTrigger(JobTriggerInfo startJobTriggerInfo, JobTriggerInfo stopJobTriggerInfo) throws SchedulerException {
		updateTrigger(startJobTriggerInfo);
		updateTrigger(stopJobTriggerInfo);
	}
	
	private void updateTrigger(JobTriggerInfo jobTriggerInfo) throws SchedulerException{
		TriggerKey triggerKey = new TriggerKey(jobTriggerInfo.getTriggerName(), jobTriggerInfo.getTriggerGroup());
		Trigger newTrigger = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				// This might not be required. Still!
				.withSchedule(CronScheduleBuilder.cronSchedule(jobTriggerInfo.getCronExpression()))
				.build();
		scheduler.updateTrigger(triggerKey, newTrigger);
	}

	/**
	 * Remove the Job and all associated triggers.
	 */
	@Auditable(AuditingEventType.EC2_SCHEDULE_DELETED)
	public void deleteJob(JobTriggerInfo startJobTriggerInfo,
			JobTriggerInfo stopJobTriggerInfo, String instanceId)
			throws SchedulerException {
		scheduler.deleteJob(startJobTriggerInfo.getJobGroup(), startJobTriggerInfo.getJobName());
		scheduler.deleteJob(stopJobTriggerInfo.getJobGroup(), stopJobTriggerInfo.getJobName());
	}


	@Override
	@Auditable(AuditingEventType.EC2_SCHEDULE_CREATED)
	public void scheduleInstance(JobTriggerInfo startJobTriggerInfo,
			JobTriggerInfo stopJobTriggerInfo, String instanceId) throws SchedulerException {
		abstractScheduleInstance(startJobTriggerInfo, instanceId);
		abstractScheduleInstance(stopJobTriggerInfo, instanceId);
	}
	
	private void abstractScheduleInstance(JobTriggerInfo jobTriggerInfo, String instanceId) throws SchedulerException{
		scheduler.createJob(jobTriggerInfo.getJobGroup(), jobTriggerInfo.getJobName(), jobTriggerInfo.getJobType(), instanceId);
		scheduler.schedule(jobTriggerInfo.getJobGroup(), jobTriggerInfo.getJobName(), jobTriggerInfo.getTriggerGroup(), 
				jobTriggerInfo.getTriggerName(), jobTriggerInfo.getCronExpression(), instanceId);
	}

}
