package com.hcentive.cloudmanage.audit;

import java.util.List;


public interface AuditService {

	public void audit(AuditEntity auditEntity);

	public List<AuditEntity> getAuditsList();

}
