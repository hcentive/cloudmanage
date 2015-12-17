package com.hcentive.cloudmanage.audit;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface AuditRepository extends CrudRepository<AuditEntity, Long> {

	@Query("SELECT t FROM AuditEntity t WHERE "
			+ "LOWER(t.args) LIKE LOWER(CONCAT('%',:instanceId, '%'))")
	public List<AuditEntity> findBySearchTerm(
			@Param("instanceId") String instanceId);

	@Query(value = "SELECT t1.* FROM AUDIT_INFO t1 LEFT JOIN AUDIT_INFO t2 ON t1.EVENT_TYPE = t2.EVENT_TYPE"
			+ " AND t1.EVENT_TIME < t2.EVENT_TIME WHERE t2.EVENT_TIME IS NULL"
			+ " AND t1.EVENT_PARAMS LIKE CONCAT('%',:instanceId, '%')"
			+ " ORDER BY t1.EVENT_TIME DESC", nativeQuery = true)
	public List<AuditEntity> findLatestDistinctBySearchTerm(@Param("instanceId") String instanceId);
}
