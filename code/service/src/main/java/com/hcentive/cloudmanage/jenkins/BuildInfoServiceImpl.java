package com.hcentive.cloudmanage.jenkins;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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

@Service("jenkinsService")
public class BuildInfoServiceImpl implements BuildInfoService {

	@Autowired
	private JenkinsClientProxy jenkinsServer;

	private static final Logger logger = LoggerFactory
			.getLogger(BuildInfoServiceImpl.class.getName());

	@Override
	public BuildInfo getBuildInfo(String jobName, int lastSuccessfulBuildNumber)
			throws IOException {
		
		String url = AppConfig.jenkinsUrl + "job/{jobName}/{lastSuccessfulBuildNumber}/api/json";
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("jobName", jobName);
		vars.put("lastSuccessfulBuildNumber", String.valueOf(lastSuccessfulBuildNumber));
		String result = restTemplate.getForObject(
				url, String.class, vars);
		BuildInfo ref = new BuildInfo(result);
		ref.setLastSuccessfulBuildID(lastSuccessfulBuildNumber);
		return ref;
	}
	
	
	@Override
	public BuildJobResponse getBuilds() throws IOException {
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		BuildJobResponse buildJobResponse = restTemplate.getForObject(AppConfig.jenkinsJobsUrl, BuildJobResponse.class);
		return buildJobResponse;
	}


	@Override
	public JobInfo getJobInfo(String jobName) throws IOException {
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		Map<String, String> vars = Collections.singletonMap("jobName", jobName);
		String url = AppConfig.jenkinsUrl + "job/{jobName}/api/json";
		JobInfo result = restTemplate.getForObject(
				url, JobInfo.class, vars);
		Integer lastSuccessfulBuild = result.getLastSuccessfulBuildNumber();
		return lastSuccessfulBuild;
	}


	@Override
	public String getLogFile(String jobName, Integer buildNumber) {
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		String url = AppConfig.jenkinsUrl + "job/{jobName}/{buildNumber}/consoleText/api/json";
		Map<String, String> vars = new HashMap<String, String>();
		vars.put("jobName", jobName);
		vars.put("buildNumber", String.valueOf(buildNumber));
		String result = restTemplate.getForObject(
				url, String.class, vars);
		return result;
	}

}
