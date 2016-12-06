package com.hcentive.cloudmanage.audit;

import java.util.List;


public interface AuditService {

	void audit(AuditEntity auditEntity);

	List<AuditEntity> getAuditsList(String instanceId);

	List<AuditEntity> getLatestDistinctAuditsList(String instanceId);

	List<AuditEntity> getAudits(Integer from,Integer pageSize,Boolean latest);

	Long countByUserName(String userName);
}
