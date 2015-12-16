package com.hcentive.cloudmanage.audit;

import java.io.Serializable;

public class AuditContext implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String initiator;

	public String getInitiator() {
		return initiator;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}
	
	

}
