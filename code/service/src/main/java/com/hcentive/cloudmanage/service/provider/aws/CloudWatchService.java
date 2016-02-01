package com.hcentive.cloudmanage.service.provider.aws;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hcentive.cloudmanage.profiling.ProfileInfo;

public interface CloudWatchService {

	public List<ProfileInfo> getMetrics(String instanceId, Date fromTime,
			Date tillTime);

	public void updateMetrics(String instanceId, Date fromTime, Date tillTime)
			throws JsonProcessingException;

}
