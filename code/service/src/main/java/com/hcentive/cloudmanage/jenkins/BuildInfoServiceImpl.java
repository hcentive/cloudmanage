package com.hcentive.cloudmanage.jenkins;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
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
	private BuildHostMapRepository buildHostMapRepository;

	@Autowired
	private HostMetaInfoRepository hostMetaInfoRepository;
	@Autowired
	private LastSuccessfulBuildRepository lastSuccessfulBuildRepository;

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
			logger.debug("Retrieved build Info from " + url + " for " + vars
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
		return result;
	}

	@Transactional
	public void updateHostNames(final String jobName, final List<Builds> builds)
			throws JsonProcessingException {
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieve hosts for " + jobName + " from " + builds);
		}
		Map<String, BuildInfo> hosts = getLastSuccessfulBuildForHosts(jobName,
				builds);
		if (logger.isDebugEnabled()) {
			logger.debug("Retrieved hosts for " + jobName + " as " + hosts);
		}
		persistHostBldMap(jobName, hosts);
		// Also update the BuildObjects for job-host identifier.
		// Beware we might have hosts without successful build.
		persistHostBldInfo(jobName, hosts);
	}

	private void persistHostBldInfo(final String jobName,
			Map<String, BuildInfo> hosts) throws JsonProcessingException {
		// Get the BuildInfo
		List<LastSuccessfulBuildInfo> response = lastSuccessfulBuildRepository
				.findByJobName(jobName);
		// Create a host map - easy to work with
		Map<String, LastSuccessfulBuildInfo> responseMap = new HashMap<String, LastSuccessfulBuildInfo>();
		for (LastSuccessfulBuildInfo bld : response) {
			responseMap.put(bld.getJobHostKey().getHostName(), bld);
		}
		// Either update or add the new hosts with build info.
		for (String host : hosts.keySet()) {
			LastSuccessfulBuildInfo lastBld = responseMap.get(host);
			BuildInfo buildInfo = hosts.get(host);
			// It could be null - means no successful build for that host, so
			// skip that
			if (buildInfo != null
					&& (lastBld == null // New Host Or Old Value
					|| lastBld.getBuildNumber() < new Integer(
							buildInfo.getBuildId()))) {

				LastSuccessfulBuildInfo ref = new LastSuccessfulBuildInfo();
				JobHostKey comKey = new JobHostKey();

				comKey.setHostName(host);
				comKey.setJobName(jobName);
				ref.setJobHostKey(comKey);

				ref.setBuildNumber(new Integer(buildInfo.getBuildId()));
				// UsingNew Value
				ref.setBuildInfoJson(buildInfo.toJson());

				responseMap.put(host, ref);
			}
			// else its the same - so skip it.
		}
		// persist.
		lastSuccessfulBuildRepository.save(responseMap.values());
		if (logger.isInfoEnabled()) {
			logger.info("Updated last successful builds for "
					+ responseMap.keySet() + " as " + responseMap.values());
		}
	}

	private void persistHostBldMap(final String jobName,
			Map<String, BuildInfo> hosts) {
		// Get the BuildInfo
		BuildMetaInfo buildMetaInfo = buildHostMapRepository
				.findByJobName(jobName);
		if (buildMetaInfo == null) {
			buildMetaInfo = new BuildMetaInfo(jobName);
		}
		if (logger.isDebugEnabled()) {
			logger.debug("Existing Build Info {}", buildMetaInfo);
		}
		// Get all the hosts
		List<HostMetaInfo> hostMetaInfoList = buildMetaInfo
				.getHostMetaInfoList();
		if (hostMetaInfoList == null) {
			hostMetaInfoList = new ArrayList<HostMetaInfo>();
		}
		// Update the list
		for (String host : hosts.keySet()) {
			// Create only if not already exist - so fetch the hosts
			HostMetaInfo hostObj = hostMetaInfoRepository.findByHostName(host);
			if (hostObj == null) {
				hostObj = new HostMetaInfo(host);
			}
			if (!hostMetaInfoList.contains(hostObj)) {
				hostMetaInfoList.add(hostObj);
			}
		}
		buildMetaInfo.setHostMetaInfoList(hostMetaInfoList);
		if (logger.isDebugEnabled()) {
			logger.debug("Updating Build Info {}", buildMetaInfo);
		}
		// use repository to update them.
		buildHostMapRepository.save(buildMetaInfo);

		if (logger.isInfoEnabled()) {
			logger.info("Updated hosts for " + jobName + " as " + buildMetaInfo);
		}
	}

	// Utility method used only from one service.
	private Map<String, BuildInfo> getLastSuccessfulBuildForHosts(
			String jobName, List<Builds> builds) {
		// For each build get the hostNames in a set till yesterday.
		Map<String, BuildInfo> hostNames = new HashMap<String, BuildInfo>();

		for (Builds build : builds) {
			SuccessfulBuild previousBuild = new JobInfo().new SuccessfulBuild();
			previousBuild.setNumber(build.getNumber());
			BuildInfo buildObj = getBuildInfo(jobName, previousBuild);

			// Till yesterday
			Calendar yesterday = Calendar.getInstance();
			yesterday.add(Calendar.DATE, AppConfig.lastDaysForHostNames);
			if (buildObj.getInitiatedAt().before(yesterday)) {
				break;
			}
			// Gather unique hostnames.
			String hostName = buildObj.getHostName();
			// Gather last successful build
			BuildInfo existingBuildInfo = hostNames.get(hostName);
			if (existingBuildInfo == null) {
				// Either its a new Host or Successful Build is missing.
				if (!buildObj.getStatus().equalsIgnoreCase("success")) {
					buildObj = null;
				}
				// Update host and build Info only if its successful.
				hostNames.put(hostName, buildObj);
			}
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
