package com.hcentive.cloudmanage.job;

import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Service;

@DisallowConcurrentExecution
@Service
public class StartEC2InstanceJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println(DateTime.now()
				+ " Called StartEC2InstanceJob : JobDetail--> "
				+ context.getJobDetail() + " & Trigger-->"
				+ context.getTrigger());
	}

}
