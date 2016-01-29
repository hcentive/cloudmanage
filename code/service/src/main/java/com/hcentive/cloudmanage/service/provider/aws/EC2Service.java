package com.hcentive.cloudmanage.service.provider.aws;

import java.util.List;
import java.util.Set;

import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.amazonaws.services.ec2.model.TerminateInstancesResult;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.JobTriggerInfo;
import com.hcentive.cloudmanage.job.InstanceJobDetails;

public interface EC2Service {

	public Instance getInstance(String instanceId);
	
	public Instance getInstanceByPrivateIP(String privateIP);
	
	public List<Instance> getInstanceLists();

	public StopInstancesResult stopInstance(String instanceId);

	public StartInstancesResult startInstance(String instanceId);
	
	public TerminateInstancesResult terminateInstance(String instanceId);

	public void updateInstanceMetaInfo();
	
	// QUARTZ SECTION *******************


	public InstanceJobDetails getInstanceJobDetails(String instanceId) throws SchedulerException;

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public Set<JobKey> listScheduledInstanceJobs() throws SchedulerException;

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public Set<TriggerKey> listScheduledInstanceTriggers()
			throws SchedulerException;

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public void updateTrigger(JobTriggerInfo startJobTriggerInfo, JobTriggerInfo stopJobTriggerInfo) throws SchedulerException;
	
	public void scheduleInstance(JobTriggerInfo startJobTriggerInfo,
			JobTriggerInfo stopJobTriggerInfo, String instanceId) throws SchedulerException;

	public void deleteJob(JobTriggerInfo startJobTriggerInfo,
			JobTriggerInfo stopJobTriggerInfo, String instanceId) throws SchedulerException;
}
