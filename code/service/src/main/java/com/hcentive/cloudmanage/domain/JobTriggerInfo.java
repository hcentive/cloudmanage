package com.hcentive.cloudmanage.domain;

public class JobTriggerInfo {
	
	private String costCenter;
	
	private String instanceId;
	
	private String type;
	
	public JobTriggerInfo(String costCenter, String instanceId, String type){
		
		this.costCenter = costCenter;
		this.instanceId = instanceId;
		this.type = type;
	}

	public String getJobName() {
		return instanceId + "_"+type;
	}


	public String getJobGroup() {
		return costCenter + "_job";
	}


	public String getTriggerName() {
		return getJobName() + "_trigger";
	}


	public String getTriggerGroup() {
		return costCenter + "_trigger";
	}


	public String getJobType() {
		return type;
	}


}
