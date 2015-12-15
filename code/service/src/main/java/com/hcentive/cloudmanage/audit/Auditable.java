package com.hcentive.cloudmanage.audit;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface Auditable {
	AuditingEventType value();

	public enum AuditingEventType {
		EC2_START, EC2_STOP, EC2_SCHEDULED_DELETED, EC2_SCHEDULED_UPDATED, EC2_SCHEDULED;
	};
}
