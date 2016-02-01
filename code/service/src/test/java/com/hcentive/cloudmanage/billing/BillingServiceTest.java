package com.hcentive.cloudmanage.billing;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
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
		instanceId = "i-0c8994de";
		// Todays data
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DATE) - 1;
		calendar.set(year, month, day, 0, 0, 0);
		Date fromTime = calendar.getTime();
		// Change as per the data is available
		calendar.set(2015, 12, day, 23, 59, 59);
		Date tillTime = calendar.getTime();
		List<BillingInfo> billingInfo = billingService.getBilling(instanceId,
				fromTime, tillTime);
		assertNotNull("Billing Info Not Available for " + instanceId,
				billingInfo.get(0));
	}

	@Test
	public void testUpdateBilling() {
		System.out.println("Update Billing");
		String accountId = "770377720390";
		int month = 12;
		int year = 2015;
		try {
			billingService.updateBilling(accountId, year, month);
			System.out.println("Updated Billing for " + accountId + " for "
					+ month + "-" + year);
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to update Billing for " + accountId
					+ " for " + month + "-" + year);
		}
	}
}
