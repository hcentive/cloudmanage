package com.hcentive.cloudmanage.jenkins;

import java.io.IOException;

import com.hcentive.cloudmanage.domain.BuildInfo;
import com.hcentive.cloudmanage.domain.BuildJobResponse;

public interface BuildInfoService {

	public BuildInfo getBuildInfo(String jobName, int lastSuccessfulBuildNumber)
			throws IOException;

	public BuildJobResponse getBuilds() throws IOException;

	public Integer getLastSuccessfulBuildNumber(String jobName) throws IOException;
	
	public String getLogFile(String jobName, Integer buildNumber);
}
