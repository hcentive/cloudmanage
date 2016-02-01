package com.hcentive.cloudmanage.billing;

import java.io.IOException;
import java.util.Date;
import java.util.List;

public interface BillingService {

	public List<BillingInfo> getBilling(String instanceId, Date fromTime,
			Date tillTime);

	public void updateBilling(String accountId, int year, int month)
			throws IOException;
}
