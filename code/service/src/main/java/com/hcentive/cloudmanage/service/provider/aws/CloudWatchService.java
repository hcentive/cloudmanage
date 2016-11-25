package com.hcentive.cloudmanage.service.provider.aws;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcentive.cloudmanage.domain.Alarm;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.profiling.CPUThresholdInfo;
import com.hcentive.cloudmanage.profiling.ProfileInfo;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Date;
import java.util.List;

public interface CloudWatchService {

	List<ProfileInfo> getMetrics(String instanceId, Date fromTime,
			Date tillTime);

	void updateMetrics(String instanceId, Date fromTime, Date tillTime)
			throws JsonProcessingException;

	List<Instance> getIneffectiveInstances(List<Instance> ec2List);

	@PreAuthorize("hasAnyAuthority(['techops'])")
	void setCPUThreshold(CPUThresholdInfo cpuThresholdInfo);

	CPUThresholdInfo getCPUThreshold(String instanceId);
	
	Alarm getAlarm(String instanceId);
	Alarm getAlarmByName(String alarmName);
	Alarm getAlarmByInstance(String instanceId);
	void createOrUpdateAlarm(Alarm alarm);
}
