package com.hcentive.cloudmanage.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcentive.cloudmanage.domain.BuildInfo;
import com.hcentive.cloudmanage.jenkins.BuildInfoService;

@RestController
@RequestMapping("/buildInfo")
public class BuildInfoController {

	@Autowired
	private BuildInfoService buildInfoService;

	@RequestMapping(value = "/jobs", method = RequestMethod.GET)
	public List<String> buildJobs() throws IOException {
		return buildInfoService.getBuilds();
	}

	@RequestMapping(value = "/{jobName}", method = RequestMethod.GET)
	public int lastSuccessfulBuild(
			@PathVariable(value = "jobName") String jobName) throws IOException {
		return buildInfoService.getLastSuccessfulBuildNumber(jobName);
	}

	@RequestMapping(value = "/{jobName}/{buildNumber}", method = RequestMethod.GET)
	public BuildInfo buildInfo(@PathVariable(value = "jobName") String jobName,
			@PathVariable(value = "buildNumber") int buildNumber)
			throws IOException {
		BuildInfo buildInfo = buildInfoService.getBuildInfo(jobName,
				buildNumber);
		return buildInfo;
	}
}
