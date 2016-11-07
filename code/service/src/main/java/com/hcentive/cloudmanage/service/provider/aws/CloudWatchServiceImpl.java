package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.cloudwatch.model.Datapoint;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmHistoryResult;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsForMetricRequest;
import com.amazonaws.services.cloudwatch.model.DescribeAlarmsForMetricResult;
import com.amazonaws.services.cloudwatch.model.Dimension;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsRequest;
import com.amazonaws.services.cloudwatch.model.GetMetricStatisticsResult;
import com.amazonaws.services.cloudwatch.model.MetricAlarm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcentive.cloudmanage.billing.AWSMetaInfo;
import com.hcentive.cloudmanage.billing.AWSMetaRepository;
import com.hcentive.cloudmanage.domain.AWSClientProxy;
import com.hcentive.cloudmanage.domain.Alarm;
import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.profiling.CPUThresholdInfo;
import com.hcentive.cloudmanage.profiling.CPUThresholdInfoRepository;
import com.hcentive.cloudmanage.profiling.ProfileInfo;
import com.hcentive.cloudmanage.profiling.ProfilingInfoRepository;

@Service("cloudWatchService")
public class CloudWatchServiceImpl implements CloudWatchService {

	private static final Logger logger = LoggerFactory
			.getLogger(CloudWatchServiceImpl.class.getName());

	// Minimum value made available.
	private static final int period = 60;
	private static final int DefaultDailyCPUThreshold = 5;

	@Autowired
	private AWSClientProxy awsClientProxy;

	@Autowired
	private EC2Service ec2Service;

	@Autowired
	private ProfilingInfoRepository awsProfileInfoRepository;

	@Autowired
	private AWSMetaRepository awsMetaRepository;

	@Autowired
	private CPUThresholdInfoRepository cpuThresholdInfoRepository;

	/**
	 * A session must be requested for each call because it has a role
	 * associated with it.
	 */
	public AmazonCloudWatchClient getCloudWatchSession(boolean applyPolicy) {
		return awsClientProxy.getCloudWatchClient(applyPolicy);
	}

	private List<Datapoint> retrieveMetrics(String statsName,
			String instanceId, Date fromTime, Date tillTime, int period) {
		GetMetricStatisticsRequest getMetricRequest = new GetMetricStatisticsRequest();
		// Only for EC2
		getMetricRequest.setNamespace("AWS/EC2");
		// Average, Minimum or Maximum
		ArrayList<String> stats = new ArrayList<String>();
		stats.add(statsName);
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
				logger.debug("CPU Stats {}: avg {} and max {}",
						dp.getTimestamp(), dp.getAverage(), dp.getMaximum());
			}
		}

		return datapoints;
	}

	@Override
	public void updateMetrics(String instanceId, Date fromTime, Date tillTime)
			throws JsonProcessingException {

		// Transform to calendar for comparison and remove hh:mm:ss:sss
		Calendar fromTimeCal = Calendar.getInstance();
		fromTimeCal.setTime(fromTime);
		fromTimeCal.set(Calendar.HOUR_OF_DAY, 0);
		fromTimeCal.set(Calendar.MINUTE, 0);
		fromTimeCal.set(Calendar.SECOND, 0);
		fromTimeCal.set(Calendar.MILLISECOND, 0);

		// Check if the entry is already there - if yes:skip
		ProfileInfo response = awsProfileInfoRepository
				.findByInstanceIdAndSnapshotAt(instanceId,
						fromTimeCal.getTime());
		if (logger.isDebugEnabled()) {
			logger.debug("Profile Info exists for {} & {}", instanceId,
					fromTimeCal.getTime());
		}
		if (response == null) {
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

				List<Datapoint> dpList = retrieveMetrics("Average", instanceId,
						fromTimeCal.getTime(), dailyWindow.getTime(), period);

				// Get ec2 info - not attempting to fetch all.
				AWSMetaInfo ec2 = awsMetaRepository
						.findByAwsInstanceId(instanceId);
				if (dpList != null && dpList.size() > 0) {
					ObjectMapper mapper = new ObjectMapper();
					ProfileInfo profileInfo = new ProfileInfo();
					profileInfo.setInstanceInfo(ec2);
					profileInfo.setSnapshotAt(fromTimeCal.getTime());
					profileInfo.setAvgCPUHourly(mapper
							.writeValueAsString(dpList));
					profileInfoList.add(profileInfo);
				}
				// Move the counter to the next Day
				fromTimeCal.add(Calendar.DATE, 1);
			}
			awsProfileInfoRepository.save(profileInfoList);
			if (logger.isDebugEnabled()) {
				logger.debug("Profile Info Ingested for {} & {}", instanceId,
						fromTimeCal.getTime());
			}
		}
	}

	@Override
	public List<ProfileInfo> getMetrics(String instanceId, Date fromTime,
			Date tillTime) {
		List<ProfileInfo> response = awsProfileInfoRepository
				.findByInstanceIdForDates(instanceId, fromTime, tillTime);
		if (logger.isInfoEnabled()) {
			logger.info(
					"Metrics for {} between {} & {} returned as ProfileInfo list of size {}",
					instanceId, fromTime, tillTime, response.size());
		}
		return response;
	}

	@Override
	// Filter list to ineffective ec2s i.e. Max in the last 24 hours <
	// configured value.
	public List<Instance> getIneffectiveInstances(List<Instance> ec2List) {
		int i = 0;
		int instancesSize = ec2List.size();
		// 24 hour window
		Calendar fromTime = Calendar.getInstance();
		fromTime.add(Calendar.HOUR_OF_DAY, -24);
		Calendar tillTime = Calendar.getInstance();
		for (Iterator<Instance> it = ec2List.iterator(); it.hasNext();) {
			Instance ec2 = it.next();
			String instanceId = ec2.getAwsInstance().getInstanceId();
			logger.info("{} effectiveness ... {} out of {}", instanceId, i++,
					instancesSize);
			List<Datapoint> dpList = retrieveMetrics("Maximum", instanceId,
					fromTime.getTime(), tillTime.getTime(), 3600 * 24);
			// Get the value to be compared if not skipped.
			CPUThresholdInfo cpuThreshold = getCPUThreshold(instanceId);
			// If threshold is defined and correct and|or skipped.
			int threshHold = DefaultDailyCPUThreshold;
			if (cpuThreshold != null) {
				if (cpuThreshold.isSkipMeFlag()) {
					// skip
					it.remove();
					logger.info("{} is safe as skipped", instanceId);
					continue;
				}
				threshHold = cpuThreshold.getDailyCPUThreshold();
				if (threshHold <= 0) {
					// Precaution wrt junk data.
					threshHold = DefaultDailyCPUThreshold;
					logger.debug(
							"Setting default value of 5% to CPU threshold for {}",
							instanceId);
				}
			}
			// A single value will be returned for a 24 hour period
			for (Datapoint dp : dpList) {
				logger.debug("Datapoint " + dp.toString());
				Double max = dp.getMaximum();
				if (max.compareTo(new Double(threshHold)) >= 0) {
					logger.info("{} is safe with {} as compared to {}",
							instanceId, max, threshHold);
					it.remove();
				} else {
					logger.info("{} is unsafe with {} as compared to {}",
							instanceId, max, threshHold);
				}
			}

		}
		return ec2List;
	}

	@Override
	/**
	 * Has Authorization requirements. Not everybody can set it.
	 */
	public void setCPUThreshold(CPUThresholdInfo cpuThresholdInfo) {
		cpuThresholdInfoRepository.save(cpuThresholdInfo);
	}

	@Override
	public CPUThresholdInfo getCPUThreshold(String instanceId) {
		CPUThresholdInfo cpuThreshold = cpuThresholdInfoRepository
				.findByInstanceId(instanceId);
		if (cpuThreshold == null) {
			boolean skipMeFlag = false;
			AWSMetaInfo instanceInfo = ec2Service.getAWSMetaInfo(instanceId);
			cpuThreshold = new CPUThresholdInfo(DefaultDailyCPUThreshold,
					skipMeFlag, instanceInfo);
		}
		return cpuThreshold;
	}
	
	@Override
	public Alarm getAlarm(String instanceId){
		AmazonCloudWatchClient cloudWatchClient = getCloudWatchSession(false);
		Dimension dimension = new Dimension();
		List<Dimension> dimensions = new ArrayList<Dimension>();
		DescribeAlarmsForMetricRequest metricRequest = new DescribeAlarmsForMetricRequest();
		DescribeAlarmsForMetricResult result;
		Alarm alarm = new Alarm();
		alarm.setEnable(false); // default set to false
		
		dimension.setName("InstanceId");
		dimension.setValue(instanceId); // instanceId for testing - i-072aabf3537e08c62
		dimensions.add(dimension);
		metricRequest.setDimensions(dimensions);
		metricRequest.setMetricName("CPUUtilization");
		metricRequest.setNamespace("AWS/EC2");
		result = cloudWatchClient.describeAlarmsForMetric(metricRequest);
		for(MetricAlarm metricAlarm : result.getMetricAlarms()){
			alarm.setName(metricAlarm.getAlarmName());
			alarm.setThreshold(metricAlarm.getThreshold());
			alarm.setFrequency(metricAlarm.getPeriod());
			alarm.setInstanceId(instanceId);
			alarm.setEnable(metricAlarm.getActionsEnabled());
		}
		return alarm;
	}
}
