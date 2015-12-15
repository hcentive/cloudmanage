package com.hcentive.cloudmanage.audit;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.hcentive.cloudmanage.service.provider.aws.SEmailService;

//@Aspect types are not automatically picked up by the component-scan mechanism (anymore)
// So add  @Component
@Aspect
@Component
public class AuditAspect {

	private static final Logger logger = LoggerFactory
			.getLogger(AuditAspect.class.getName());

	@Autowired
	private AuditService auditService;

	@Autowired
	private SEmailService sEmailService;

	/**
	 * Audit all @annotation marked interface operations.
	 * 
	 * @param jp
	 * @param auditable
	 */
	@After("execution(@com.hcentive.cloudmanage.audit.Auditable * *(..)) && @annotation(auditable)")
	public void logAction(JoinPoint jp, Auditable auditable) throws Throwable {
		// Event Data
		String eventType = auditable.value().name();
		StringBuilder strBld = new StringBuilder("[");
		Object[] args = jp.getArgs();
		for (Object arg : args) {
			if (strBld.length() > 1) {
				strBld.append(", ");
			}
			strBld.append(arg);
		}
		strBld.append("]");
		// User
		String auth = SecurityContextHolder.getContext().getAuthentication()
				.getName();
		// Save
		AuditEntity audit = new AuditEntity(eventType, strBld.toString(), auth);
		auditService.audit(audit);
		// Log
		logger.info("AuditEntity =" + audit);
		// Email
		sEmailService.notifyViaEmail(audit);
	}
}
