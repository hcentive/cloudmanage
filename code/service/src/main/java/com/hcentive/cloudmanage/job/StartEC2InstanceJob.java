package com.hcentive.cloudmanage.job;

import org.joda.time.DateTime;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.hcentive.cloudmanage.audit.AuditContext;
import com.hcentive.cloudmanage.audit.AuditContextHolder;
import com.hcentive.cloudmanage.service.provider.aws.AWSUtils;
import com.hcentive.cloudmanage.service.provider.aws.EC2Service;

@DisallowConcurrentExecution
@Service
public class StartEC2InstanceJob implements Job {
	
	private static final Logger logger = LoggerFactory
			.getLogger(StartEC2InstanceJob.class.getName());

	@Autowired
	private EC2Service ec2Service;

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		logger.info(DateTime.now()
				+ " Called StartEC2InstanceJob : JobDetail--> "
				+ context.getJobDetail() + " & Trigger-->"
				+ context.getTrigger());
		JobDetail jobDetail = context.getJobDetail();
		AuditContext auditContext = new AuditContext();
		auditContext.setInitiator(jobDetail.getKey().getName());
		AuditContextHolder.setContext(auditContext);
		StartInstancesResult startedInstance = ec2Service.startInstance(jobDetail.getJobDataMap().getString(AWSUtils.INSTANCE_ID));
		logger.info(startedInstance.toString());
		
	}

}
