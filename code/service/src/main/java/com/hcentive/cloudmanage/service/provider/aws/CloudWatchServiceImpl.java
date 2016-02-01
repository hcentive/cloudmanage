package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcentive.cloudmanage.billing.AWSMetaInfo;
import com.hcentive.cloudmanage.billing.AWSMetaRepository;
import com.hcentive.cloudmanage.domain.AWSClientProxy;
import com.hcentive.cloudmanage.profiling.ProfileInfo;
import com.hcentive.cloudmanage.profiling.ProfilingInfoRepository;

@Service("cloudWatchService")
public class CloudWatchServiceImpl implements CloudWatchService {

	private static final Logger logger = LoggerFactory
			.getLogger(CloudWatchServiceImpl.class.getName());

	@Autowired
	private AWSClientProxy awsClientProxy;

	@Autowired
	private ProfilingInfoRepository awsProfileInfoRepository;

	@Autowired
	private AWSMetaRepository awsMetaRepository;

	/**
	 * A session must be requested for each call because it has a role
	 * associated with it.
	 */
	public AmazonCloudWatchClient getCloudWatchSession(boolean applyPolicy) {
		return awsClientProxy.getCloudWatchClient(applyPolicy);
	}

	private List<Datapoint> retrieveMetrics(String instanceId, Date fromTime,
			Date tillTime, int period) {
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
		getMetricRequest.withStartTime(fromTime);
		// till time
		getMetricRequest.withEndTime(tillTime);

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

	@Override
	public void updateMetrics(String instanceId, Date fromTime, Date tillTime)
			throws JsonProcessingException {
		int period = 60; // Minimum value made available.

		// Transform to calendar for comparison and remove hh:mm:ss:sss
		Calendar fromTimeCal = Calendar.getInstance();
		fromTimeCal.setTime(fromTime);
		fromTimeCal.set(Calendar.HOUR_OF_DAY, 0);
		fromTimeCal.set(Calendar.MINUTE, 0);
		fromTimeCal.set(Calendar.SECOND, 0);
		fromTimeCal.set(Calendar.MILLISECOND, 0);

		Calendar tillTimeCal = Calendar.getInstance();
		tillTimeCal.setTime(tillTime);
		tillTimeCal.set(Calendar.HOUR_OF_DAY, 23);
		tillTimeCal.set(Calendar.MINUTE, 59);
		tillTimeCal.set(Calendar.SECOND, 59);
		tillTimeCal.set(Calendar.MILLISECOND, 999);

		// Loop per day
		List<ProfileInfo> profileInfoList = new ArrayList<ProfileInfo>();
		while (tillTimeCal.after(fromTimeCal)) {

			Calendar dailyWindow = (Calendar) fromTimeCal.clone();
			dailyWindow.set(Calendar.HOUR_OF_DAY, 23);
			dailyWindow.set(Calendar.MINUTE, 59);
			dailyWindow.set(Calendar.SECOND, 59);
			dailyWindow.set(Calendar.MILLISECOND, 999);

			List<Datapoint> dpList = retrieveMetrics(instanceId,
					fromTimeCal.getTime(), dailyWindow.getTime(), period);

			// Get ec2 info - not attempting to fetch all.
			AWSMetaInfo ec2 = awsMetaRepository.findByAwsInstanceId(instanceId);
			if (dpList != null && dpList.size() > 0) {
				ObjectMapper mapper = new ObjectMapper();
				ProfileInfo profileInfo = new ProfileInfo();
				profileInfo.setInstanceInfo(ec2);
				profileInfo.setSnapshotAt(fromTimeCal.getTime());
				profileInfo.setAvgCPUHourly(mapper.writeValueAsString(dpList));
				profileInfoList.add(profileInfo);
			}
			// Move the counter to the next Day
			fromTimeCal.add(Calendar.DATE, 1);
		}
		awsProfileInfoRepository.save(profileInfoList);
	}

	@Override
	public List<ProfileInfo> getMetrics(String instanceId, Date fromTime,
			Date tillTime) {
		return awsProfileInfoRepository.findByInstanceIdForDates(instanceId,
				fromTime, tillTime);
	}

}
