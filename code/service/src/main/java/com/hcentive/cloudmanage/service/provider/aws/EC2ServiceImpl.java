package com.hcentive.cloudmanage.service.provider.aws;

import com.amazonaws.services.ec2.AmazonEC2Client;
import com.amazonaws.services.ec2.model.*;
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
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("ec2Service")
public class EC2ServiceImpl implements EC2Service {

	private static final Logger logger = LoggerFactory
			.getLogger(EC2ServiceImpl.class.getName());
	
	private static final String STACK = "Stack";

	@Autowired
	private AWSClientProxy awsClientProxy;

	@Autowired
	private DecisionMapperService decisionMapperService;

	@Autowired
	private AWSMetaRepository awsMetaRepository;

	@Autowired
	private DNSService dNSService;

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
	 * Lists EC2 Instance.
	 */
	public Instance getInstance(String instanceId) {
		return getInstance(instanceId, false);
	}

	public Instance getInstanceForJob(String instanceId, boolean jobContext) {
		return getInstance(instanceId, true);
	}

	public Instance getInstance(String instanceId, boolean jobContext) {
		logger.info("Instance info");
		DescribeInstancesRequest request = new DescribeInstancesRequest()
				.withInstanceIds(instanceId).withFilters(
						jobContext ? null : getDecisionMapAsFilters());
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
		List<Instance> instances = new ArrayList<Instance>();
		DescribeInstancesRequest instanceRequest = new DescribeInstancesRequest()
				.withFilters(jobContext ? null : getDecisionMapAsFilters());
		DescribeInstancesResult instancesResult = getEC2Session(false)
				.describeInstances(instanceRequest);
		if (logger.isDebugEnabled()) {
			logger.debug("Received {} instances", instancesResult
					.getReservations().size());
		}
		instances = AWSUtils.extractInstances(instancesResult);
		logger.info("Returning {} instances", instances.size());
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
	public StopInstancesResult stopInstance(String instanceId,
			boolean jobContext) {
		logger.info("Stopping instance " + instanceId);
		// Check if the rootDeviceType is 'ebs' or 'instance store'.
		StopInstancesRequest stopRequest = new StopInstancesRequest()
				.withInstanceIds(instanceId);
		StopInstancesResult stoppedInstance = null;
		boolean stoppable = isStoppable(instanceId);
		String env = System.getProperty("env");
		if (!env.equalsIgnoreCase("dev") && stoppable) {
			logger.info(
					"Stopping Instance {} in env {}; stoppable {}; from job {}",
					instanceId, env, stoppable, jobContext);
			stoppedInstance = getEC2Session(
					SecurityContextHolder.getContext().getAuthentication() != null)
					.stopInstances(stopRequest);
		} else {
			logger.info(
					"Request to stop {} skipped=> env {}; stoppable {}; from job {}",
					instanceId, env, stoppable, jobContext);
		}
		logger.info("Instance stopped " + stoppedInstance);
		return stoppedInstance;
	}

	/*
	 * Makes sure only dev and qa are actually stopped and that too in non-dev
	 * environment.
	 */
	private boolean isStoppable(String instanceId) {
		boolean stoppable = false;
		Instance instance = getInstanceForJob(instanceId, true);
		for (Tag tag : instance.getAwsInstance().getTags()) {
			if (tag.getKey().equals("Stack")) {
				if (tag.getValue().equals("dev") || tag.getValue().equals("qa")) {
					stoppable = true;
				}
			}
		}
		return stoppable;
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
			logger.info("updateInstanceMetaInfo triggered for {} instances",
					instances.size());
		}

		// Retrieve all existing entities from database.
		Iterable<AWSMetaInfo> awsMetaInstances = awsMetaRepository.findAll();
		Map<String, AWSMetaInfo> awsMetaInstanceMap = new HashMap<String, AWSMetaInfo>();
		for (AWSMetaInfo awsMetaInstance : awsMetaInstances) {
			awsMetaInstanceMap.put(awsMetaInstance.getAwsInstanceId(),
					awsMetaInstance);
		}

		// Retrieve DNS entries from Route 53.
		Map<String, String> dnsMap = dNSService.getDNS();

		// Round up all existing objects.
		List<AWSMetaInfo> entitiesToSave = new ArrayList<AWSMetaInfo>();
		for (Instance instance : instances) {
			if (logger.isDebugEnabled()) {
				logger.debug("AWS Instance being considered for update {}",
						instance.getAwsInstance().getInstanceId());
			}

			// First check if we have an instance of the same id.
			com.amazonaws.services.ec2.model.Instance ec2Instance = instance
					.getAwsInstance();
			String awsId = ec2Instance.getInstanceId();
			// Existing Object if available in DB.
			AWSMetaInfo awsMetaInfo = awsMetaInstanceMap.get(awsId);

			boolean restrictTagUpdate = false;
			// In case of new instance or new aws-Id: It is to be saved.
			if (awsMetaInfo == null) {
				awsMetaInfo = new AWSMetaInfo();
				awsMetaInfo.setAwsInstanceId(awsId);
			} else { // Check the old ones for 2 attributes
				if (!isStackAndCostCenterMatching(awsMetaInfo, ec2Instance)) {
					// TODO
					// restrictTagUpdate = true;
				}
			}
			// Update all properties
			// IP also required to fetch the DNS name.
			String privateIpAddress = ec2Instance.getPrivateIpAddress();
			awsMetaInfo.setPrivateIP(privateIpAddress);
			String publicIpAddress = ec2Instance.getPublicIpAddress();
			awsMetaInfo.setPublicIP(publicIpAddress);
			String dnsName = dnsMap.get(privateIpAddress);
			// fallback to public
			if (dnsName == null) {
				dnsMap.get(publicIpAddress);
			}
			awsMetaInfo.setDnsName(dnsName);
			awsMetaInfo.setInstanceType(ec2Instance.getInstanceType());
			awsMetaInfo.setLaunchTime(ec2Instance.getLaunchTime());
			for (com.amazonaws.services.ec2.model.Tag tag : ec2Instance
					.getTags()) {
				String tagKey = tag.getKey();
				if (tagKey.equalsIgnoreCase("Name")) {
					awsMetaInfo.setName(tag.getValue());
				} else if (tagKey.equalsIgnoreCase("Product")) {
					awsMetaInfo.setProduct(tag.getValue());
				} else if (tagKey.equalsIgnoreCase("Client")) {
					awsMetaInfo.setClient(tag.getValue());
				} else if (tagKey.equalsIgnoreCase("Stack")) {
					awsMetaInfo.setStack(tag.getValue()
							+ (restrictTagUpdate ? "*" : ""));
				} else if (tagKey.equalsIgnoreCase("Owner")) {
					awsMetaInfo.setOwner(tag.getValue());
				} else if (tagKey.equalsIgnoreCase("Cost-Center")) {
					awsMetaInfo.setCostCenter(tag.getValue()
							+ (restrictTagUpdate ? "*" : ""));
				}
			}
			entitiesToSave.add(awsMetaInfo);
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

	private boolean isStackAndCostCenterMatching(AWSMetaInfo awsMetaInfo,
			com.amazonaws.services.ec2.model.Instance ec2Instance) {
		// Ensure that there be no change in either tags
		// Begin with matching and find if different.
		boolean match = true;

		String costCenter1 = awsMetaInfo.getCostCenter();
		String stack1 = awsMetaInfo.getStack();

		String costCenter2 = null;
		String stack2 = null;

		for (com.amazonaws.services.ec2.model.Tag tag : ec2Instance.getTags()) {
			String tagKey = tag.getKey();
			if (tagKey.equalsIgnoreCase("Cost-Center")) {
				costCenter2 = tag.getValue();
			} else if (tagKey.equalsIgnoreCase("Stack")) {
				stack2 = tag.getValue();
			}
		}

		if (costCenter1 != null && !costCenter1.equalsIgnoreCase(costCenter2)) {
			match = false;
		}
		if (stack1 != null && !stack1.equals(stack2)) {
			match = false;
		}

		if (!match) {
			logger.warn(
					"Changes observed in immutable TAG values of Stack and Cost-center"
							+ " {},{} in DB as compared to {},{} in AWS for {}. Contact Administrator.",
					stack1, costCenter1, stack2, costCenter2,
					ec2Instance.getInstanceId());
		}
		return match;
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

	@Override
	public boolean isTagPresent(Instance instance,String tagKey,Set<String> tagSet) {
		Optional<Tag> optionalTag = instance.getAwsInstance()
				.getTags().stream().filter(tag -> tag.getKey().equals(tagKey)).findFirst();

		return optionalTag.isPresent() && tagSet.contains(optionalTag.get().getValue().toLowerCase());
	}

	@Override
	public boolean isTagPresent(Instance instance,String tagKey){
		Optional<Tag> optionalTag = instance.getAwsInstance()
				.getTags().stream().filter(tag -> tag.getKey().equals(tagKey)).findAny();

		return optionalTag.isPresent();
	}

	@Override
	public void createTag(String tagKey,String tagValue,String resource){
		if(tagKey == null || tagKey.isEmpty()) throw new IllegalArgumentException("Tag Key cannot be null or empty");
		if(tagValue == null || tagValue.isEmpty()) throw new IllegalArgumentException("Tag value cannot be null or empty");
		if(resource == null || resource.isEmpty()) throw new IllegalArgumentException("Resource cannot be null or empty");

		AmazonEC2Client ec2Client = getEC2Session(false);
		CreateTagsRequest request = new CreateTagsRequest()
				.withResources(resource)
				.withTags(new Tag().withKey(tagKey).withValue(tagValue));
		ec2Client.createTags(request);
	}

	@Override
	public void deleteTag(String tagKey,String resource){
		if(tagKey == null || tagKey.isEmpty()) throw new IllegalArgumentException("Tag Key cannot be null or empty");
		if(resource == null || resource.isEmpty()) throw new IllegalArgumentException("Resource cannot be null or empty");

		AmazonEC2Client ec2Client = getEC2Session(false);
		DeleteTagsRequest request = new DeleteTagsRequest()
				.withResources(resource)
				.withTags(new Tag().withKey(tagKey));
		ec2Client.deleteTags(request);
	}
}
