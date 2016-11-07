package com.hcentive.cloudmanage.service.provider.aws;

import java.util.Date;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcentive.cloudmanage.domain.Alarm;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.profiling.CPUThresholdInfo;
import com.hcentive.cloudmanage.profiling.ProfileInfo;

public interface CloudWatchService {

	public List<ProfileInfo> getMetrics(String instanceId, Date fromTime,
			Date tillTime);

	public void updateMetrics(String instanceId, Date fromTime, Date tillTime)
			throws JsonProcessingException;

	public List<Instance> getIneffectiveInstances(List<Instance> ec2List);

	@PreAuthorize("hasAnyAuthority(['techops'])")
	public void setCPUThreshold(CPUThresholdInfo cpuThresholdInfo);

	public CPUThresholdInfo getCPUThreshold(String instanceId);
	
	public Alarm getAlarm(String instanceId);
}
