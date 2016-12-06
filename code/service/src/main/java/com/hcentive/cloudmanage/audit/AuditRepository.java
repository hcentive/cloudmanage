package com.hcentive.cloudmanage.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditRepository extends JpaRepository<AuditEntity, Long> {

	@Query("SELECT t FROM AuditEntity t WHERE "
			+ "LOWER(t.args) LIKE LOWER(CONCAT('%',:instanceId, '%'))")
	List<AuditEntity> findBySearchTerm(
			@Param("instanceId") String instanceId);

	@Query(value = "SELECT t1.* FROM AUDIT_INFO t1 LEFT JOIN AUDIT_INFO t2 ON t1.EVENT_TYPE = t2.EVENT_TYPE"
			+ " AND t1.EVENT_TIME < t2.EVENT_TIME WHERE t2.EVENT_TIME IS NULL"
			+ " AND t1.EVENT_PARAMS LIKE CONCAT('%',:instanceId, '%')"
			+ " ORDER BY t1.EVENT_TIME DESC", nativeQuery = true)
	List<AuditEntity> findLatestDistinctBySearchTerm(@Param("instanceId") String instanceId);

	Long countByUserName(String userName);

	Page<AuditEntity> findByUserName(String userName, Pageable pageable);
}
