package com.hcentive.cloudmanage.jenkins;

import com.hcentive.cloudmanage.domain.BuildInfo;
import com.hcentive.cloudmanage.domain.BuildJobResponse;
import com.hcentive.cloudmanage.domain.JobInfo;
import com.hcentive.cloudmanage.domain.JobInfo.SuccessfulBuild;

public interface BuildInfoService {

	public BuildJobResponse getBuilds();

	public JobInfo getJobInfo(String jobName);

	public String getLogFile(String jobName, Integer buildNumber);

	public BuildInfo getBuildInfo(String jobName,
			SuccessfulBuild successfulBuild);
}
