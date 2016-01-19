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
//		HttpMethod method = new GetMethod(AppConfig.jenkinsUrl + "job" + "/"
//				+ jobName + "/" + lastSuccessfulBuildNumber
//				+ "/api/json?pretty=true");
//		session.executeMethod(method);
//		checkResult(method.getStatusCode());
//		String jsonResponse = method.getResponseBodyAsString();
//		
//
//		// Get Log file as well.
//		HttpMethod logMethod = new GetMethod(AppConfig.jenkinsUrl + "job" + "/"
//				+ jobName + "/" + lastSuccessfulBuildNumber
//				+ "/consoleText/api/json?pretty=true");
//		session.executeMethod(logMethod);
//		checkResult(logMethod.getStatusCode());
//		String logFile = logMethod.getResponseBodyAsString();
//		// Write it to file system.
//		File file = new File(AppConfig.logBaseDir + ref.getLogFileLocation());
//		// if file doesn't exists, then create it
//		if (!file.exists()) {
//			file.createNewFile();
//		}
//		BufferedWriter bw = new BufferedWriter(new FileWriter(
//				file.getAbsoluteFile()));
//		bw.write(logFile);

		return ref;
	}
	
	
	@Override
	public BuildJobResponse getBuilds() throws IOException {
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		BuildJobResponse buildJobResponse = restTemplate.getForObject(AppConfig.jenkinsJobsUrl, BuildJobResponse.class);
		return buildJobResponse;
	}


	@Override
	public Integer getLastSuccessfulBuildNumber(String jobName) throws IOException {
		RestTemplate restTemplate = jenkinsServer.getRestTemplate();
		Map<String, String> vars = Collections.singletonMap("jobName", jobName);
		String url = AppConfig.jenkinsUrl + "job/{jobName}/api/json";
		JobInfo result = restTemplate.getForObject(
				url, JobInfo.class, vars);
		Map<String, String> lastSuccessfulBuild = result.getLastSuccessfulBuild();
		if(lastSuccessfulBuild != null){
			return Integer.parseInt(lastSuccessfulBuild.get("number"));
		}
		return null;
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
