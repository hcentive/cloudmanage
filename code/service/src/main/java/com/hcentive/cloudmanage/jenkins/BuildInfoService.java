package com.hcentive.cloudmanage.jenkins;

import java.io.IOException;

import com.hcentive.cloudmanage.domain.BuildInfo;
import com.hcentive.cloudmanage.domain.BuildJobResponse;
import com.hcentive.cloudmanage.domain.JobInfo;
import com.hcentive.cloudmanage.domain.JobInfo.SuccessfulBuild;

public interface BuildInfoService {

	public BuildJobResponse getBuilds() throws IOException;

	public JobInfo getJobInfo(String jobName) throws IOException;
	
	public String getLogFile(String jobName, Integer buildNumber);

	BuildInfo getBuildInfo(String jobName, SuccessfulBuild successfulBuild)
			throws IOException;
}
