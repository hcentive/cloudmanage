package com.hcentive.cloudmanage.audit;

import java.util.List;


public interface AuditService {

	public void audit(AuditEntity auditEntity);

	public List<AuditEntity> getAuditsList();

	public List<AuditEntity> getAuditsList(String instanceId);

	public List<AuditEntity> getLatestDistinctAuditsList(String instanceId);
}
