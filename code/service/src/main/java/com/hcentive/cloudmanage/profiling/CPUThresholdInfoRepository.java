package com.hcentive.cloudmanage.profiling;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface CPUThresholdInfoRepository extends
		CrudRepository<CPUThresholdInfo, Long> {

	@Query("select b from CPUThresholdInfo b where b.instanceInfo.awsInstanceId = :instanceId")
	public CPUThresholdInfo findByInstanceId(
			@Param("instanceId") String instanceId);
}
