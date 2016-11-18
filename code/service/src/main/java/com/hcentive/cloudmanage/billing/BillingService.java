package com.hcentive.cloudmanage.billing;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface BillingService {

	public List<BillingInfo> getBilling(String instanceId, Date fromTime,
			Date tillTime);

	public List<BillingInfo> getBilling(Date fromTime, Date tillTime);

	public void updateBilling(File billFile) throws IOException;

	public List<String> billsIngested();

	public void markBillIngested(String billFile);

	BigDecimal getBillingCost(String instanceId, Date fromDate, Date toDate);
}
