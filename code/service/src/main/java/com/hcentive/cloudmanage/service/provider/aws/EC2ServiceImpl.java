package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.amazonaws.Request;
import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Filter;
import com.amazonaws.services.ec2.model.StartInstancesRequest;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesRequest;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesRequest;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.hcentive.cloudmanage.audit.Auditable;
import com.hcentive.cloudmanage.audit.Auditable.AuditingEventType;
import com.hcentive.cloudmanage.billing.AWSMetaInfo;
import com.hcentive.cloudmanage.billing.AWSMetaRepository;
import com.hcentive.cloudmanage.domain.AWSClientProxy;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.JobTriggerInfo;
import com.hcentive.cloudmanage.job.DynamicJobScheduler;
import com.hcentive.cloudmanage.job.InstanceJobDetails;
import com.hcentive.cloudmanage.security.DecisionMapper;

@Service("ec2Service")
public class EC2ServiceImpl implements EC2Service {

	private static final Logger logger = LoggerFactory
			.getLogger(EC2ServiceImpl.class.getName());

	@Autowired
	private AWSClientProxy awsClientProxy;

	@Autowired
	private DecisionMapperService decisionMapperService;

	@Autowired
	private AWSMetaRepository awsMetaRepository;

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

	@Override
	public Instance getInstanceByPrivateIP(String privateIP) {
		logger.info("Instance info");
		DescribeInstancesRequest request = new DescribeInstancesRequest()
				.withFilters(getFilter(
						"network-interface.addresses.private-ip-address",
						privateIP));
		DescribeInstancesResult instanceResult = getEC2Session(false)
				.describeInstances(request);
		Instance instance = AWSUtils.extractInstance(instanceResult);
		return instance;

	}

	private Filter getFilter(String key, String value) {
		Filter filter = new Filter(key).withValues(value);
		return filter;
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
		Set<DecisionMapper> decisionMapper = decisionMapperService
				.getDecisionMap();
		return decisionMapper;
	}

	/**
	 * Lists AWS bucket.
	 */
	public Instance getInstance(String instanceId) {
		logger.info("Instance info");
		DescribeInstancesRequest request = new DescribeInstancesRequest()
				.withInstanceIds(instanceId).withFilters(
						getDecisionMapAsFilters());
		DescribeInstancesResult instanceResult = getEC2Session(false)
				.describeInstances(request);
		Instance instance = AWSUtils.extractInstance(instanceResult);
		return instance;
	}

	/**
	 * Lists EC2 Instances.
	 */
	public List<Instance> getInstanceLists(boolean jobContext) {
		logger.info("Listing instances");
		List<Instance> instances = null;
		DescribeInstancesRequest instanceRequest = new DescribeInstancesRequest()
				.withFilters(jobContext ? null : getDecisionMapAsFilters());
		DescribeInstancesResult instancesResult = getEC2Session(false)
				.describeInstances(instanceRequest);
		instances = AWSUtils.extractInstances(instancesResult);
		return instances;
	}

	@Override
	public List<Instance> getRunningInstances(boolean jobContext) {
		logger.info("Listing running instances");
		List<Instance> instances = null;
		List<Filter> filters = new ArrayList<Filter>();
		filters.add(new Filter().withName("instance-state-name").withValues(
				"running"));
		// If not called from JOB
		if (!jobContext) {
			filters.addAll(getDecisionMapAsFilters());
		}
		DescribeInstancesRequest instanceRequest = new DescribeInstancesRequest()
				.withFilters(filters);
		DescribeInstancesResult instancesResult = getEC2Session(false)
				.describeInstances(instanceRequest);
		instances = AWSUtils.extractInstances(instancesResult);
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
		StopInstancesResult stoppedInstance = null;
		if (!System.getProperty("env").equalsIgnoreCase("dev")) {
			logger.info("Stopping Instance {}", instanceId);
			/*
			 * stoppedInstance = getEC2Session(
			 * SecurityContextHolder.getContext().getAuthentication() != null)
			 * .stopInstances(stopRequest);
			 */
		} else {
			logger.info("Request to stop {} skipped in dev", instanceId);
		}
		logger.info("Instance stopped " + stoppedInstance);
		return stoppedInstance;
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
		StartInstancesResult startedInstances = getEC2Session(
				SecurityContextHolder.getContext().getAuthentication() != null)
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

	public void updateInstanceMetaInfo(boolean jobContext) {
		List<Instance> instances = getInstanceLists(jobContext);
		if (logger.isInfoEnabled()) {
			logger.info("updateInstanceMetaInfo triggered for {}", instances);
		}

		// Retrieve all existing entities from database.
		Iterable<AWSMetaInfo> awsMetaInstances = awsMetaRepository.findAll();
		Map<String, AWSMetaInfo> awsMetaInstanceMap = new HashMap<String, AWSMetaInfo>();
		for (AWSMetaInfo awsMetaInstance : awsMetaInstances) {
			awsMetaInstanceMap.put(awsMetaInstance.getAwsInstanceId(),
					awsMetaInstance);
		}
		// Round up all existing objects.
		List<AWSMetaInfo> entitiesToSave = new ArrayList<AWSMetaInfo>();
		for (Instance instance : instances) {
			if (logger.isDebugEnabled()) {
				logger.debug("Instance being considered for update {}",
						instance);
			}
			// New Object.
			AWSMetaInfo metaInfo = new AWSMetaInfo();
			// Data elements
			com.amazonaws.services.ec2.model.Instance ec2Instance = instance
					.getAwsInstance();
			String awsId = ec2Instance.getInstanceId();
			metaInfo.setAwsInstanceId(awsId);
			metaInfo.setPrivateIP(ec2Instance.getPrivateIpAddress());
			metaInfo.setPublicIP(ec2Instance.getPublicIpAddress());
			metaInfo.setInstanceType(ec2Instance.getInstanceType());
			metaInfo.setLaunchTime(ec2Instance.getLaunchTime());
			for (com.amazonaws.services.ec2.model.Tag tag : ec2Instance
					.getTags()) {
				String tagKey = tag.getKey();
				if (tagKey.equalsIgnoreCase("cost-center")) {
					metaInfo.setCostCenter(tag.getValue());
				} else if (tagKey.equalsIgnoreCase("stack")) {
					metaInfo.setStack(tag.getValue());
				} else if (tagKey.equalsIgnoreCase("name")) {
					metaInfo.setName(tag.getValue());
				} else if (tagKey.equalsIgnoreCase("project")) {
					metaInfo.setProject(tag.getValue());
				} else if (tagKey.equalsIgnoreCase("owner")) {
					metaInfo.setOwner(tag.getValue());
				}
			}
			// Existing Object if available.
			AWSMetaInfo awsMetaInfo = awsMetaInstanceMap.get(awsId);
			// Ensure that there be no change in either tags
			// Begin with matching and find if different.
			boolean match = true;
			if (awsMetaInfo != null) {
				// All other attributes can change except cost center and stack

				// For cost center
				String costCenter1 = awsMetaInfo.getCostCenter();
				String costCenter2 = metaInfo.getCostCenter();
				if (costCenter1 != null
						&& !costCenter1.equalsIgnoreCase(costCenter2)) {
					match = false;
				}

				// Same for stack
				String stack1 = awsMetaInfo.getStack();
				String stack2 = metaInfo.getStack();
				if (stack1 != null && !stack1.equals(stack2)) {
					match = false;
				}

				if (!match) {
					logger.warn(
							"Changes observed in immutable TAG values of stack and cost-center"
									+ " {},{} as compared to {},{} for EC2 {}. Contact Administrator.",
							stack2, costCenter2, stack1, costCenter1, awsId);
					// Skip this instance as it would violate unique constraint.
					continue;
				}
			} else {
				// Could not find the instance so its not matching
				match = false;
			}
			// Add them to the database only if its not matching.
			if (!match) {
				entitiesToSave.add(metaInfo);
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Updating aws ec2 meta info master with "
					+ entitiesToSave);
		}
		// Iterate and save 1-by-1 as some of them might fail.
		for (AWSMetaInfo entityToSave : entitiesToSave) {
			try {
				awsMetaRepository.save(entityToSave);
			} catch (Exception e) {
				logger.error(
						"Skipping: Failed to update aws ec2 meta info master for {}:{} ",
						entityToSave.getAwsInstanceId(), e);
			}
		}
		if (logger.isInfoEnabled()) {
			logger.info("Updated aws ec2 meta info master list with "
					+ entitiesToSave.size() + " items: " + entitiesToSave);
		}
	}

	// ****************** QUARTZ ***********************//

	@Autowired
	private DynamicJobScheduler scheduler;

	public InstanceJobDetails getInstanceJobDetails(String instanceId)
			throws SchedulerException {
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
	public void updateTrigger(JobTriggerInfo startJobTriggerInfo,
			JobTriggerInfo stopJobTriggerInfo) throws SchedulerException {
		updateTrigger(startJobTriggerInfo);
		updateTrigger(stopJobTriggerInfo);
	}

	private void updateTrigger(JobTriggerInfo jobTriggerInfo)
			throws SchedulerException {
		TriggerKey triggerKey = new TriggerKey(jobTriggerInfo.getTriggerName(),
				jobTriggerInfo.getTriggerGroup());
		Trigger newTrigger = TriggerBuilder.newTrigger()
				.withIdentity(triggerKey)
				// This might not be required. Still!
				.withSchedule(
						CronScheduleBuilder.cronSchedule(jobTriggerInfo
								.getCronExpression())).build();
		scheduler.updateTrigger(triggerKey, newTrigger);
	}

	/**
	 * Remove the Job and all associated triggers.
	 */
	@Auditable(AuditingEventType.EC2_SCHEDULE_DELETED)
	public void deleteJob(JobTriggerInfo startJobTriggerInfo,
			JobTriggerInfo stopJobTriggerInfo, String instanceId)
			throws SchedulerException {
		scheduler.deleteJob(startJobTriggerInfo.getJobGroup(),
				startJobTriggerInfo.getJobName());
		scheduler.deleteJob(stopJobTriggerInfo.getJobGroup(),
				stopJobTriggerInfo.getJobName());
	}

	@Override
	@Auditable(AuditingEventType.EC2_SCHEDULE_CREATED)
	public void scheduleInstance(JobTriggerInfo startJobTriggerInfo,
			JobTriggerInfo stopJobTriggerInfo, String instanceId)
			throws SchedulerException {
		abstractScheduleInstance(startJobTriggerInfo, instanceId);
		abstractScheduleInstance(stopJobTriggerInfo, instanceId);
	}

	private void abstractScheduleInstance(JobTriggerInfo jobTriggerInfo,
			String instanceId) throws SchedulerException {
		scheduler.createJob(jobTriggerInfo.getJobGroup(),
				jobTriggerInfo.getJobName(), jobTriggerInfo.getJobType(),
				instanceId);
		scheduler.schedule(jobTriggerInfo.getJobGroup(),
				jobTriggerInfo.getJobName(), jobTriggerInfo.getTriggerGroup(),
				jobTriggerInfo.getTriggerName(),
				jobTriggerInfo.getCronExpression(), instanceId);
	}

	@Override
	public List<AWSMetaInfo> getAWSMetaInfoList() {
		List<AWSMetaInfo> list = new ArrayList<AWSMetaInfo>();
		Iterable<AWSMetaInfo> awsMetaInfos = awsMetaRepository.findAll();
		for (AWSMetaInfo awsMetaInfo : awsMetaInfos) {
			list.add(awsMetaInfo);
		}
		return list;
	}

	@Override
	public AWSMetaInfo getAWSMetaInfo(String instanceId) {
		AWSMetaInfo awsMetaInfo = awsMetaRepository
				.findByAwsInstanceId(instanceId);
		return awsMetaInfo;
	}
}
