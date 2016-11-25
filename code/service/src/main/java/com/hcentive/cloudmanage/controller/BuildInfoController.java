package com.hcentive.cloudmanage.controller;

import com.hcentive.cloudmanage.jenkins.BuildInfoService;
import com.hcentive.cloudmanage.jenkins.BuildMetaInfo;
import com.hcentive.cloudmanage.jenkins.LastSuccessfulBuildInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Api(basePath = "/build",
		value = "build",
		description = "Api endpoint related to Jenkins build",
		produces = "application/json",
		tags = "build")
@RestController
@RequestMapping("/build")
public class BuildInfoController {

	@Autowired
	private BuildInfoService buildInfoService;

	@ApiOperation(value = "Get all jenkins build jobs name",
		nickname = "Get all jenkins build jobs name")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public List<String> buildJobs() throws IOException {
		List<BuildMetaInfo> buildMetaInfoList = buildInfoService.getAllBuilds();
		return getJobNames(buildMetaInfoList);
	}

	private List<String> getJobNames(List<BuildMetaInfo> buildMetaInfoList){
		return buildMetaInfoList.stream().map(BuildMetaInfo::getJobName).collect(Collectors.toList());
	}
	
	@ApiOperation(value = "Get last successful build information",
		nickname = "Get last successful build information")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "jobName",value = "Job Name",required = true,dataType = "string",paramType = "path")
	})
	@RequestMapping(value = "/{jobName:.+}", method = RequestMethod.GET)
	public List<LastSuccessfulBuildInfo> getLastSuccessfulBuildInfo(@PathVariable(value = "jobName") String jobName)
			throws IOException {
		
		List<LastSuccessfulBuildInfo> lastSuccessfulBuildInfoList = buildInfoService.getLastSuccessBuildInfo(jobName);
		return lastSuccessfulBuildInfoList;
	}

	@ApiOperation(value = "Get jenkins build log")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "jobName",value = "Job Name",required = true,dataType = "string",paramType = "path"),
			@ApiImplicitParam(name = "buildNumber",value = "Build Number",required = true,dataType = "int",paramType = "path")
	})
	@RequestMapping(value = "/log/{jobName}/{buildNumber}", method = RequestMethod.GET)
	public void downloadBuildLog(@PathVariable(value="jobName") String jobName, @PathVariable(value="buildNumber") Integer buildNumber, HttpServletResponse response) throws IOException{
		String logFileContents = buildInfoService.getLogFile(jobName, buildNumber);
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", String.format("inline; filename=\"" + jobName +"_"+buildNumber+".log\""));
		FileCopyUtils.copy(logFileContents, response.getWriter());
	}
}
