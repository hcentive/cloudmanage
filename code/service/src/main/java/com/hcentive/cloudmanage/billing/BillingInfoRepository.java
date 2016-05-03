package com.hcentive.cloudmanage.billing;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface BillingInfoRepository extends
		CrudRepository<BillingInfo, Long> {

	@Query("select b from BillingInfo b where b.instanceInfo.awsInstanceId = :instanceId and b.snapshotAt between :fromDate and :tillDate")
	public List<BillingInfo> findByInstanceId(
			@Param("instanceId") String instanceId,
			@Param("fromDate") Date fromDate, @Param("tillDate") Date tillDate);
	
	@Query("select b from BillingInfo b where b.snapshotAt between :fromDate and :tillDate")
	public List<BillingInfo> findByPeriod(
			@Param("fromDate") Date fromDate, @Param("tillDate") Date tillDate);

}
