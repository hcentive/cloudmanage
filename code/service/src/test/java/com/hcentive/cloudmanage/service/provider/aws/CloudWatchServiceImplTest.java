package com.hcentive.cloudmanage.service.provider.aws;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hcentive.cloudmanage.Application;
import com.hcentive.cloudmanage.profiling.ProfileInfo;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource("classpath:config/application-test.properties")
public class CloudWatchServiceImplTest {

	@Autowired
	private CloudWatchService cloudWatchService;

	// POC cloud manage instance for testing.
	private String instanceName = "i-02cfdf29";

	@Test
	public void testGetMetrics() {
		try {
			// Todays data
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE) - 1;
			calendar.set(year, month, day, 0, 0, 0);
			Date fromTime = calendar.getTime();
			calendar.set(year, month, day, 23, 59, 59);
			Date tillTime = calendar.getTime();
			List<ProfileInfo> response = cloudWatchService.getMetrics(
					instanceName, fromTime, tillTime);
			System.out.println("Response " + response);
			// Expecting 1 day's response.
			assertNotNull("Not able to retrieve metrics ", response.get(0));
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not able to retrieve ec2 info.");
		}
	}

	@Test
	public void testUpdateMetrics() {
		try {
			// Todays data
			Calendar calendar = Calendar.getInstance();
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DATE) - 1;
			calendar.set(year, month, day-1, 0, 0, 0);
			Date fromTime = calendar.getTime();
			calendar.set(year, month, day, 23, 59, 59);
			Date tillTime = calendar.getTime();
			System.out.println("Update Metrics for " + fromTime + "--"
					+ tillTime);
			cloudWatchService.updateMetrics(instanceName, fromTime, tillTime);
			System.out.println("Updated Metrics for instance " + instanceName);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not able to retrieve ec2 metrics for " + instanceName);
		}
	}
}
