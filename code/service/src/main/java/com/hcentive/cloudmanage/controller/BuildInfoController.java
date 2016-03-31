package com.hcentive.cloudmanage.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcentive.cloudmanage.domain.BuildInfo;
import com.hcentive.cloudmanage.domain.BuildJobResponse;
import com.hcentive.cloudmanage.domain.JobInfo;
import com.hcentive.cloudmanage.domain.JobMetaInfo;
import com.hcentive.cloudmanage.jenkins.BuildInfoService;
import com.hcentive.cloudmanage.jenkins.BuildMetaInfo;
import com.hcentive.cloudmanage.jenkins.LastSuccessfulBuildInfo;

@RestController
@RequestMapping("/build")
public class BuildInfoController {

	@Autowired
	private BuildInfoService buildInfoService;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<String> buildJobs() throws IOException {
		List<BuildMetaInfo> buildMetaInfoList = buildInfoService.getAllBuilds();
		List<String> jobNames  = getJobNames(buildMetaInfoList);
		return jobNames;
	}
	
	
	private List<String> getJobNames(List<BuildMetaInfo> buildMetaInfoList){
		List<String> jobNames = buildMetaInfoList.stream().map(new Function<BuildMetaInfo, String>(){
			@Override
			public String apply(BuildMetaInfo buildMetaInfo){
				return buildMetaInfo.getJobName();
			}
		}).collect(Collectors.toList());
		return jobNames;
		
	}
	

	@RequestMapping(value = "/{jobName:.+}", method = RequestMethod.GET)
	public List<LastSuccessfulBuildInfo> getLastSuccessfulBuildInfo(@PathVariable(value = "jobName") String jobName)
			throws IOException {
		
		List<LastSuccessfulBuildInfo> lastSuccessfulBuildInfoList = buildInfoService.getLastSuccessBuildInfo(jobName);
		return lastSuccessfulBuildInfoList;
	}
	
	@RequestMapping(value = "/log/{jobName}/{buildNumber}", method = RequestMethod.GET)
	public void downloadBuildLog(@PathVariable(value="jobName") String jobName, @PathVariable(value="buildNumber") Integer buildNumber, HttpServletResponse response) throws IOException{
		String logFileContents = buildInfoService.getLogFile(jobName, buildNumber);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + jobName +"_"+buildNumber+".log\""));
		FileCopyUtils.copy(logFileContents, response.getWriter());
	}
}
