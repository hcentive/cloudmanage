package com.hcentive.cloudmanage.jenkins;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hcentive.cloudmanage.AppConfig;
import com.hcentive.cloudmanage.domain.BuildInfo;
import com.hcentive.cloudmanage.domain.BuildJobResponse;
import com.hcentive.cloudmanage.domain.JenkinsClientProxy;
import com.hcentive.cloudmanage.domain.JobInfo;
import com.hcentive.cloudmanage.domain.JobInfo.Builds;
import com.hcentive.cloudmanage.domain.JobInfo.SuccessfulBuild;

@Service("jenkinsService")
public class BuildInfoServiceImpl implements BuildInfoService {

	@Autowired
	private JenkinsClientProxy jenkinsServer;

	@Autowired
	private BuildInfoRepository buildInfoDao;

	private static final Logger logger = LoggerFactory
			.getLogger(BuildInfoServiceImpl.class.getName());

	@Override
	public BuildInfo getBuildInfo(String jobName,
			SuccessfulBuild successfulBuild) {
		Integer lastSuccessfulBuildNumber = successfulBuild.getNumber();
		String url = AppConfig.jenkinsUrl
				+ "job/{jobName}/{lastSuccessfulBuildNumber}/api/json";
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("jobName", jobName);
		vars.put("lastSuccessfulBuildNumber",
				String.valueOf(lastSuccessfulBuildNumber));
		if (logger.isDebugEnabled()) {
			logger.debug("Retriving build Info for " + vars + " from " + url);
		}
		String result = restTemplate.getForObject(url, String.class, vars);
		BuildInfo ref = new BuildInfo(result);
		ref.setLastSuccessfulBuildID(lastSuccessfulBuildNumber);
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieved biuld Info from " + url + " for " + vars
					+ " : " + ref);
		}
		return ref;
	}

	@Override
	public BuildJobResponse getBuilds() {
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		if (logger.isDebugEnabled()) {
			logger.debug("Retriving build list from "
					+ AppConfig.jenkinsJobsUrl);
		}
		BuildJobResponse buildJobResponse = restTemplate.getForObject(
				AppConfig.jenkinsJobsUrl, BuildJobResponse.class);
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieved build list from "
					+ AppConfig.jenkinsJobsUrl + " : " + buildJobResponse);
		}
		return buildJobResponse;
	}

	@Override
	public JobInfo getJobInfo(String jobName) {
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		Map<String, String> vars = Collections.singletonMap("jobName", jobName);
		String url = AppConfig.jenkinsUrl + "job/{jobName}/api/json";
		if (logger.isDebugEnabled()) {
			logger.debug("Retriving build Info for " + vars + " from " + url);
		}
		JobInfo result = restTemplate.getForObject(url, JobInfo.class, vars);
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieved build Info from " + url + " for " + vars
					+ " : " + result);
		}

		// Use separate thread to update the same.
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				updateHostNames(jobName, result.getBuilds());
			}
		});
		t.start();
		return result;
	}

	// Not exposed via Interface.
	public void updateHostNames(final String jobName, final List<Builds> builds) {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieve hosts for " + jobName + " from " + builds);
		}
		Set<String> hosts = getHostNames(jobName, builds);
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieved hosts for " + jobName + " as " + hosts);
		}
		// Get the BuildInfo
		BuildMetaInfo buildMetaInfo = buildInfoDao.findByJobName(jobName);
		if (buildMetaInfo == null) {
			buildMetaInfo = new BuildMetaInfo(jobName);
		}
		// Get all the hosts
		List<HostMetaInfo> hostMetaInfoList = buildMetaInfo
				.getHostMetaInfoList();
		if (hostMetaInfoList == null) {
			hostMetaInfoList = new ArrayList<HostMetaInfo>();
		}
		// Update the list
		for (String host : hosts) {
			HostMetaInfo hostObj = new HostMetaInfo(host);
			if (!hostMetaInfoList.contains(hostObj)) {
				hostMetaInfoList.add(hostObj);
			}
		}
		buildMetaInfo.setHostMetaInfoList(hostMetaInfoList);
		// use repository to update them.
		buildInfoDao.save(buildMetaInfo);
		if (logger.isDebugEnabled()) {
			logger.info("Updated hosts for " + jobName + " as " + buildMetaInfo);
		}
	}

	// Utility method used only from one service.
	private Set<String> getHostNames(String jobName, List<Builds> builds) {
		// For each build get the hostNames in a set till yesterday.
		Set<String> hostNames = new HashSet<String>();

		for (Builds build : builds) {
			SuccessfulBuild previousBuild = new JobInfo().new SuccessfulBuild();
			previousBuild.setNumber(build.getNumber());
			BuildInfo buildObj = getBuildInfo(jobName, previousBuild);
			// FIXME
			if (AppConfig.lastDaysForHostNames == null) {
				System.out.println("This is Insane!");
				AppConfig.lastDaysForHostNames = -1;
			}
			// Till yesterday
			Calendar yesterday = Calendar.getInstance();
			yesterday.add(Calendar.DATE, AppConfig.lastDaysForHostNames);
			if (buildObj.getInitiatedAt().before(yesterday)) {
				break;
			}
			// Gather unique hostnames.
			String hostName = buildObj.getHostName();
			hostNames.add(hostName);
		}
		return hostNames;
	}

	@Override
	public String getLogFile(String jobName, Integer buildNumber) {
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		String url = AppConfig.jenkinsUrl
				+ "job/{jobName}/{buildNumber}/consoleText/api/json";
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("jobName", jobName);
		vars.put("buildNumber", String.valueOf(buildNumber));
		if (logger.isDebugEnabled()) {
			logger.debug("Retriving build log for " + vars + " from " + url);
		}
		String result = restTemplate.getForObject(url, String.class, vars);
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieved build log from " + url + " for " + vars
					+ " : " + result);
		}
		return result;
	}
}
