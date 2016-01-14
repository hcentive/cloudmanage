package com.hcentive.cloudmanage.jenkins;

import java.io.IOException;
import java.util.List;

import com.hcentive.cloudmanage.domain.BuildInfo;

public interface BuildInfoService {

	public BuildInfo getBuildInfo(String jobName, int lastSuccessfulBuildNumber)
			throws IOException;

	public List<String> getBuilds() throws IOException;

	public int getLastSuccessfulBuildNumber(String jobName) throws IOException;
}
