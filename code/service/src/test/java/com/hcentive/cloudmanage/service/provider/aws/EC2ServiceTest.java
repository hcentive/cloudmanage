package com.hcentive.cloudmanage.service.provider.aws;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.amazonaws.services.ec2.model.Reservation;
import com.amazonaws.services.ec2.model.StartInstancesResult;
import com.amazonaws.services.ec2.model.StopInstancesResult;
import com.hcentive.cloudmanage.Application;
import com.hcentive.cloudmanage.domain.Instance;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource("classpath:config/application-test.properties")
public class EC2ServiceTest {

	@Autowired
	private EC2Service ec2Service;

	// POC cloud manage instance for testing.
	private String instanceName = "i-0d79408c";

	@Before
	public void setup() {
		// Security stuff
		Authentication authentication = Mockito.mock(Authentication.class);
		// Mockito.whens() for your authorization object
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(
				authentication);
		SecurityContextHolder.setContext(securityContext);
	}

	@Test
	public void testGetInstance() {
		try {
			String instanceId = "i-7318ff89";
			Instance response = ec2Service.getInstance(instanceId);
			assertEquals("Not able to retrieve instance " + instanceId,
					instanceId, response.getAwsInstance().getInstanceId());
			// System.out.println("Response " + response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not able to retrieve ec2 info.");
		}
	}

	@Test
	public void testGetInstanceLists() {
		try {
			List<Instance> response = ec2Service.getInstanceLists(false);
			assertTrue("Not able to retrieve instances ", response.size() > 500);
			// System.out.println("Response " + response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not able to retrieve ec2 info.");
		}
	}

	@Test
	public void testStopInstance() {
		try {
			String instanceId = instanceName; // find a machine to start and
												// stop
			StopInstancesResult response = ec2Service.stopInstance(instanceId,
					true);
			assertEquals("Not able to stop instance" + instanceId, instanceId,
					response.getStoppingInstances().get(0).getInstanceId());
			// System.out.println("Response " + response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not able to stop ec2.");
		}
	}

	@Test
	public void testStartInstance() {
		try {
			String instanceId = instanceName; // find a machine to start and
												// stop
			StartInstancesResult response = ec2Service
					.startInstance(instanceId);
			assertEquals("Not able to start instance" + instanceId, instanceId,
					response.getStartingInstances().get(0).getInstanceId());
			// System.out.println("Response " + response);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not able to start ec2.");
		}
	}

	@Test
	public void testUpdateInstanceMetaInfo() {
		try {
			ec2Service.updateInstanceMetaInfo(true);
		} catch (Exception e) {
			e.printStackTrace();
			fail("Not able to update the ec2 meta info list.");
		}
	}

	// JOB'S tests

	@Test
	public void testTerminateInstance() {
		fail("Should never be implemented");
	}

	@Test
	public void testGetInstanceJobDetails() {
		fail("Not yet implemented");
	}

	@Test
	public void testListScheduledInstanceJobs() {
		fail("Not yet implemented");
	}

	@Test
	public void testListScheduledInstanceTriggers() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateTrigger() {
		fail("Not yet implemented");
	}

	@Test
	public void testScheduleInstance() {
		fail("Not yet implemented");
	}

	@Test
	public void testDeleteJob() {
		fail("Not yet implemented");
	}

}
