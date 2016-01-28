package com.hcentive.cloudmanage.billing;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.hcentive.cloudmanage.Application;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = Application.class)
@WebAppConfiguration
@TestPropertySource("classpath:config/application-test.properties")
public class BillingServiceTest {

	@Autowired
	private BillingService billingService;

	private String instanceId = "i-0d79408c";

	@Test
	public void testGetBilling() {
		System.out.println("Instance " + billingService);
		BillingInfo billingInfo = billingService.getBilling(instanceId);
		assertNotNull("Billing Info Not Available for " + instanceId,
				billingInfo);
	}
}
