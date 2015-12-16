package com.hcentive.cloudmanage.job;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.service.provider.aws.AWSUtils;

@Service
public class DynamicJobScheduler {

	@Autowired
	private SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private StartEC2InstanceJob startEC2InstanceJob;
	@Autowired
	private StopEC2InstanceJob stopEC2InstanceJob;

	/**
	 * Puts everything together i.e. Job & Trigger.
	 * 
	 * @param jobGroup
	 * @param jobName
	 * @param triggerGroup
	 * @param triggerName
	 * @param cronExpression
	 * @param jobType
	 * @throws SchedulerException
	 */
	@SuppressWarnings("unchecked")
	public void schedule(String jobGroup, String jobName, String triggerGroup,
			String triggerName, String cronExpression, String instanceId)
			throws SchedulerException {
		// Fetch Job Detail
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		JobKey jobKey = new JobKey(jobName, jobGroup);
		JobDetail jobDetail = scheduler.getJobDetail(jobKey);
		jobDetail.getJobDataMap().put(AWSUtils.INSTANCE_ID, instanceId);
		// Create Trigger Detail
		CronTrigger cronTrigger = TriggerBuilder.newTrigger()
				.withIdentity(triggerName, triggerGroup)
				.withSchedule(CronScheduleBuilder.cronSchedule(cronExpression))
				.build();

		// Associate with existing triggers of this job.
		List<CronTrigger> triggersForJob = (List<CronTrigger>) scheduler
				.getTriggersOfJob(jobKey);
		// Add the supplied new one.
		triggersForJob.add(cronTrigger);

		// Attach
		scheduler.scheduleJob(jobDetail, new HashSet<CronTrigger>(
				triggersForJob), true);
		// Start the Scheduler from stand by mode.
		scheduler.start();
	}

	public JobDetail createJob(String jobGroup, String jobName, String jobType, String instanceId)
			throws SchedulerException {

		Class<? extends Job> classRef;
		switch (jobType) {
		case "start-ec2":
			classRef = StartEC2InstanceJob.class;
			break;
		case "stop-ec2":
			classRef = StopEC2InstanceJob.class;
			break;
		default:
			throw new RuntimeException("Job not found " + jobName);
		}
		JobDetail jobDetail = JobBuilder.newJob(classRef)
				.withIdentity(jobName, jobGroup).storeDurably().build();
		
		jobDetail.getJobDataMap().put(AWSUtils.INSTANCE_ID, instanceId);
		// Add them to scheduler.
		schedulerFactoryBean.getScheduler().addJob(jobDetail, true);
		return jobDetail;
	}

	public Set<JobKey> listScheduledInstanceJobs() throws SchedulerException {
		Set<JobKey> jobKeys = new HashSet<>();
		List<String> jobGroupNames = schedulerFactoryBean.getScheduler()
				.getJobGroupNames();
		for (String jobGroupName : jobGroupNames) {
			GroupMatcher<JobKey> jobGroupMatcher = GroupMatcher
					.jobGroupEquals(jobGroupName);
			Set<JobKey> jobKeysForGroup = schedulerFactoryBean.getScheduler()
					.getJobKeys(jobGroupMatcher);
			System.out.println(" - " + jobKeysForGroup);
			jobKeys.addAll(jobKeysForGroup);
		}
		return jobKeys;
	}

	public Set<TriggerKey> listScheduledInstanceTriggers()
			throws SchedulerException {
		Set<TriggerKey> triggerKeys = new HashSet<>();
		List<String> triggerGroupNames = schedulerFactoryBean.getScheduler()
				.getTriggerGroupNames();
		for (String triggerGroupName : triggerGroupNames) {
			GroupMatcher<TriggerKey> triggerGroupMatcher = GroupMatcher
					.triggerGroupEquals(triggerGroupName);
			Set<TriggerKey> triggerKeysForGroup = schedulerFactoryBean
					.getScheduler().getTriggerKeys(triggerGroupMatcher);
			System.out.println(" - " + triggerKeysForGroup);
			triggerKeys.addAll(triggerKeysForGroup);
		}
		return triggerKeys;
	}

	public void updateTrigger(TriggerKey triggerKey, Trigger newTrigger)
			throws SchedulerException {
		schedulerFactoryBean.getScheduler().rescheduleJob(triggerKey,
				newTrigger);
	}

	public boolean deleteJob(String jobGroup, String jobName)
			throws SchedulerException {
		return schedulerFactoryBean.getScheduler().deleteJob(
				new JobKey(jobName, jobGroup));
	}

	public boolean deleteTrigger(String triggerGroup, String triggerName)
			throws SchedulerException {
		return schedulerFactoryBean.getScheduler().unscheduleJob(
				new TriggerKey(triggerName, triggerGroup));
	}

}