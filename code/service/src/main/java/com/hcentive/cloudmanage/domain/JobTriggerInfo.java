package com.hcentive.cloudmanage.domain;

public class JobTriggerInfo {
	
	private String costCenter;
	
	private String instanceId;
	
	private String type;
	
	private String cronExpression;
	
	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

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

	@Override
	public String toString() {
		return "JobTriggerInfo [costCenter=" + costCenter + ", instanceId="
				+ instanceId + ", type=" + type + ", cronExpression="
				+ cronExpression + "]";
	}

}
