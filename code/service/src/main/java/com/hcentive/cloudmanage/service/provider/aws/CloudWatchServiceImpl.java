package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.hcentive.cloudmanage.domain.AWSClientProxy;

public class CloudWatchServiceImpl implements CloudWatchService {

	private static final Logger logger = LoggerFactory
			.getLogger(CloudWatchServiceImpl.class.getName());

	@Autowired
	private AWSClientProxy awsClientProxy;

	/**
	 * A session must be requested for each call because it has a role
	 * associated with it.
	 */
	public AmazonCloudWatchClient getCloudWatchSession(boolean applyPolicy) {
		return awsClientProxy.getCloudWatchClient(applyPolicy);
	}

	/**
	 * Provides list of data points which has 'average cpu utilization' for a
	 * given node.
	 * 
	 * @param instanceId
	 *            - node for which metrics has been requested.
	 * @param fromTime
	 *            - start time for metrics
	 * @param tillTime
	 *            - end time for metrics
	 * @param period
	 *            - frequency of data expected in seconds.
	 * @return - sorted list of data per timestamp.
	 */
	public List<Datapoint> getMetrics(String instanceId, Calendar fromTime,
			Calendar tillTime, int period) {
		GetMetricStatisticsRequest getMetricRequest = new GetMetricStatisticsRequest();
		// Only for EC2
		getMetricRequest.setNamespace("AWS/EC2");
		// Average, Minimum or Maximum
		ArrayList<String> stats = new ArrayList<String>();
		stats.add("Average");
		getMetricRequest.setStatistics(stats);
		// CPU
		getMetricRequest.withMetricName("CPUUtilization");
		// Input parameters.
		// InstanceId as Dimension
		ArrayList<Dimension> dimensions = new ArrayList<Dimension>();
		dimensions.add(new Dimension().withName("InstanceId").withValue(
				instanceId));
		getMetricRequest.setDimensions(dimensions);
		// Period aka frequency of data capture
		getMetricRequest.setPeriod(period);
		// from time
		getMetricRequest.withStartTime(fromTime.getTime());
		// till time
		getMetricRequest.withEndTime(tillTime.getTime());

		GetMetricStatisticsResult response = getCloudWatchSession(false)
				.getMetricStatistics(getMetricRequest);

		if (logger.isDebugEnabled()) {
			logger.debug("Response returned " + response);
		}
		// Sorting is not guaranteed.
		List<Datapoint> datapoints = response.getDatapoints();
		Collections.sort(datapoints, new Comparator<Datapoint>() {
			@Override
			public int compare(Datapoint dp1, Datapoint dp2) {
				return dp1.getTimestamp().compareTo(dp2.getTimestamp());
			}
		});

		if (logger.isDebugEnabled()) {
			for (Datapoint dp : datapoints) {
				logger.debug("Avg CPU " + dp.getTimestamp() + ":"
						+ dp.getAverage());
			}
		}

		return datapoints;
	}
}
