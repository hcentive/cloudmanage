package com.hcentive.cloudmanage.service.provider.aws;

import java.util.List;
import java.util.Set;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.quartz.TriggerKey;
import org.springframework.security.access.prepost.PreAuthorize;

import com.amazonaws.services.ec2.model.Reservation;

public interface EC2Service {

	@PreAuthorize("hasAuthority('Operator')")
	public Reservation getInstance(String instanceId);

	@PreAuthorize("hasAuthority('Operator')")
	public List<Reservation> getInstanceLists();

	@PreAuthorize("hasAuthority('techops')")
	public String stopInstance(String instanceId);

	@PreAuthorize("hasAuthority('techops')")
	public String startInstance(String instanceId);

	// QUARTZ SECTION *******************

	@PreAuthorize("hasAuthority('techops')")
	public String scheduleInstance(String jobGroup, String jobName,
			String triggerGroup, String triggerName, String cronExpression);

	@PreAuthorize("hasAuthority('techops')")
	public JobDetail createJob(String jobGroup, String jobName, String jobType)
			throws SchedulerException;

	@PreAuthorize("hasAuthority('techops')")
	public Set<JobKey> listScheduledInstanceJobs() throws SchedulerException;

	@PreAuthorize("hasAuthority('techops')")
	public Set<TriggerKey> listScheduledInstanceTriggers()
			throws SchedulerException;

	@PreAuthorize("hasAuthority('techops')")
	public void updateTrigger(String triggerGroup, String triggerName,
			String cronExpression) throws SchedulerException;

	@PreAuthorize("hasAuthority('techops')")
	public boolean deleteJob(String jobGroup, String jobName)
			throws SchedulerException;

	@PreAuthorize("hasAuthority('techops')")
	public boolean deleteTrigger(String triggerGroup, String triggerName)
			throws SchedulerException;
}
