package com.hcentive.cloudmanage.profiling;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ProfilingInfoRepository extends
		CrudRepository<ProfileInfo, Long> {

	@Query("select b from ProfileInfo b where b.instanceInfo.awsInstanceId = :instanceId and b.snapshotAt between :fromDate and :tillDate")
	public List<ProfileInfo> findByInstanceIdForDates(
			@Param("instanceId") String instanceId,
			@Param("fromDate") Date fromDate, @Param("tillDate") Date tillDate);
}
