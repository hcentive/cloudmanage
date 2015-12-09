package com.hcentive.cloudmanage.service.provider.aws;

import java.util.List;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;

import com.amazonaws.services.ec2.model.Reservation;
import com.hcentive.cloudmanage.domain.Instance;

public interface EC2Service {

	public Reservation getInstance(String instanceId);

	public List<Instance> getInstanceLists();

	public String stopInstance(String instanceId);

	public String startInstance(String instanceId);
	
	public String terminateInstance(String instanceId);

	// QUARTZ SECTION *******************

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public String scheduleInstance(String jobGroup, String jobName,
			String triggerGroup, String triggerName, String cronExpression);

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public JobDetail createJob(String jobGroup, String jobName, String jobType)
			throws SchedulerException;

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public Set<JobKey> listScheduledInstanceJobs() throws SchedulerException;

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public Set<TriggerKey> listScheduledInstanceTriggers()
			throws SchedulerException;

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public void updateTrigger(String triggerGroup, String triggerName,
			String cronExpression) throws SchedulerException;

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public boolean deleteJob(String jobGroup, String jobName)
			throws SchedulerException;

	//@PreAuthorize("hasAnyAuthority(['techops','techops-int'])")
	public boolean deleteTrigger(String triggerGroup, String triggerName)
			throws SchedulerException;
}
