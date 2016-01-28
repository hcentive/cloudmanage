package com.hcentive.cloudmanage.billing;

import org.springframework.data.repository.CrudRepository;

public interface BillingInfoRepository extends
		CrudRepository<BillingInfo, Long> {
}
