package com.hcentive.cloudmanage.audit;


public class AuditContextHolder {
	
	private static final ThreadLocal<AuditContext> contextHolder = new ThreadLocal<AuditContext>();
	
	public static void setContext(AuditContext auditContext){
		contextHolder.set(auditContext);
	}
	
	public static AuditContext getContext(){
		return contextHolder.get();
	}

}
