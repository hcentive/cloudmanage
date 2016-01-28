package com.hcentive.cloudmanage.jenkins;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hcentive.cloudmanage.Application;
import com.hcentive.cloudmanage.domain.BuildInfo;
import com.hcentive.cloudmanage.domain.BuildJobResponse;
import com.hcentive.cloudmanage.domain.JobInfo;
import com.hcentive.cloudmanage.domain.JobInfo.SuccessfulBuild;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource("classpath:config/application-test.properties")
public class BuildInfoServiceTest {

	@Autowired
	private BuildInfoService buildInfoService;

	private String jobName = "wem_deploy_complete";
	private Integer buildNumber = 3498;

	@Test
	public void testGetBuilds() {
		BuildJobResponse response = null;
		try {
			response = buildInfoService.getBuilds();
		} catch (Exception e) {
			fail("Builds info not available " + e);
		}
		assertNotNull("Build info list is not available", response.getJobs()
				.size() > 0);
	}

	@Test
	public void testGetJobInfo() {
		JobInfo response = null;
		try {
			response = buildInfoService.getJobInfo(jobName);
		} catch (Exception e) {
			fail("Builds info for " + jobName + " is not available " + e);
		}
		assertNotNull("Build info description is not available",
				response.getDescription());
		assertNotNull("Last Successful Build number is not available", response
				.getLastSuccessfulBuild().getNumber());
		try {
			// Give time to have the host names updated in Database.
			Thread.currentThread().sleep(30000);
		} catch (InterruptedException e) {
			System.out.println("Could not be saved!");
			e.printStackTrace();
		}

	}

	@Test
	public void testGetLogFile() throws IOException {
		String fileContent = null;
		try {
			fileContent = buildInfoService.getLogFile(jobName, buildNumber);
		} catch (Exception e) {
			fail("Builds log for " + jobName + ":" + buildNumber
					+ " is not available " + e);
		}
		
		File f = new File(jobName + "_" + buildNumber + ".log");
		if (f.exists()) {
			assertTrue("File" + f + " is 0 bytes", f.length() > 0);
			assertEquals("File Contents are not equal",
					FileUtils.readFileToString(f, "utf-8"), fileContent);
		} else {
			fail("File Not found " + f.getAbsolutePath());
		}
	}

	@Test
	public void testGetBuildInfo() {
		BuildInfo buildInfo = null;
		try {
			SuccessfulBuild successfulBuild = new JobInfo().new SuccessfulBuild();
			successfulBuild.setNumber(buildNumber);
			buildInfo = buildInfoService.getBuildInfo(jobName, successfulBuild);
		} catch (Exception e) {
			fail("Builds info for " + jobName + " is not available " + e);
		}
		assertNotNull("Build Info Not available for " + jobName,
				buildInfo.toString());
		System.out.println("build Info " + buildInfo);
	}
}
