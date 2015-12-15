package com.hcentive.cloudmanage.service.provider.aws;

import com.hcentive.cloudmanage.audit.AuditEntity;

public interface SEmailService {

	public void notifyViaEmail(AuditEntity audit);
}
