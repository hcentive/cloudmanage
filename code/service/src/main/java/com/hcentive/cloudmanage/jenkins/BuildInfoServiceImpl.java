package com.hcentive.cloudmanage.jenkins;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.AppConfig;
import com.hcentive.cloudmanage.domain.BuildInfo;
import com.hcentive.cloudmanage.domain.JenkinsClientProxy;

@Service("jenkinsService")
public class BuildInfoServiceImpl implements BuildInfoService {

	@Autowired
	private JenkinsClientProxy jenkinsServer;

	private static final Logger logger = LoggerFactory
			.getLogger(BuildInfoServiceImpl.class.getName());

	@Override
	public BuildInfo getBuildInfo(String jobName, int lastSuccessfulBuildNumber)
			throws IOException {
		HttpClient session = jenkinsServer.getJenkinsClient();

		HttpMethod method = new GetMethod(AppConfig.jenkinsUrl + "job" + "/"
				+ jobName + "/" + lastSuccessfulBuildNumber
				+ "/api/json?pretty=true");
		session.executeMethod(method);
		checkResult(method.getStatusCode());
		String jsonResponse = method.getResponseBodyAsString();
		BuildInfo ref = new BuildInfo(jsonResponse);

		// Get Log file as well.
		HttpMethod logMethod = new GetMethod(AppConfig.jenkinsUrl + "job" + "/"
				+ jobName + "/" + lastSuccessfulBuildNumber
				+ "/consoleText/api/json?pretty=true");
		session.executeMethod(logMethod);
		checkResult(logMethod.getStatusCode());
		String logFile = logMethod.getResponseBodyAsString();
		// Write it to file system.
		File file = new File(AppConfig.logBaseDir + ref.getLogFileLocation());
		// if file doesn't exists, then create it
		if (!file.exists()) {
			file.createNewFile();
		}
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				file.getAbsoluteFile()));
		bw.write(logFile);

		return ref;
	}

	@Override
	public List<String> getBuilds() throws IOException {

		List<String> buildNames = new ArrayList<>();

		HttpClient session = jenkinsServer.getJenkinsClient();

		HttpMethod methodJobs = new GetMethod(AppConfig.jenkinsJobsUrl);
		session.executeMethod(methodJobs);
		checkResult(methodJobs.getStatusCode());
		// Could be a large response
		InputStreamReader in = new InputStreamReader(
				methodJobs.getResponseBodyAsStream(), "UTF-8");
		StringWriter sw = new StringWriter();
		int x;
		while ((x = in.read()) != -1) {
			sw.write(x);
		}
		in.close();
		// Using streams
		JSONObject jsonObj = new JSONObject(sw.toString());
		JSONArray jobs = (JSONArray) jsonObj.get("jobs");
		for (int i = 0; i < jobs.length(); i++) {
			Object jobObj = jobs.get(i);
			if (jobObj instanceof JSONObject) {
				JSONObject jobJsonObj = (JSONObject) jobObj;
				buildNames.add(jobJsonObj.getString("name"));
			}
		}
		return null;
	}

	@Override
	public int getLastSuccessfulBuildNumber(String jobName) throws IOException {
		int buildNumber = 0;

		HttpClient session = jenkinsServer.getJenkinsClient();

		HttpMethod methodJob = new GetMethod(AppConfig.jenkinsUrl + "job" + "/"
				+ jobName + "/api/json?pretty=true");
		session.executeMethod(methodJob);
		checkResult(methodJob.getStatusCode());
		String jsonResponse = methodJob.getResponseBodyAsString();
		JSONObject jsonObj = new JSONObject(jsonResponse);
		Object object = jsonObj.get("lastSuccessfulBuild");
		if (object instanceof JSONObject) {
			JSONObject jobJsonObj = (JSONObject) object;
			buildNumber = jobJsonObj.getInt("number");
		}
		return buildNumber;
	}

	/*
	 * Checks if the http code is 200 or not!
	 */
	private static void checkResult(int i) throws IOException {
		if (i / 100 != 2)
			throw new IOException();
	}
}
