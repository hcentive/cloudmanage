package com.hcentive.cloudmanage.service.provider.aws;

import java.util.Date;
import java.util.List;

import com.amazonaws.services.cloudwatch.model.Datapoint;

public interface CloudWatchService {
	
	List<Datapoint> getMetrics(String instanceId, Date fromTime, Date tillTime,
			int period);

}
