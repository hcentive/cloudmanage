package com.hcentive.cloudmanage.service.provider.aws;

import java.util.Calendar;
import java.util.List;

import com.amazonaws.services.cloudwatch.model.Datapoint;

public interface CloudWatchService {
	
	public List<Datapoint> getMetrics(String instanceId,
			Calendar fromTime, Calendar tillTime, int period);

}
