package com.hcentive.cloudmanage.billing;

public interface BillingService {

	public BillingInfo getBilling(String instanceId);

	public void updateBilling(String accountId, int year, int month);
}
