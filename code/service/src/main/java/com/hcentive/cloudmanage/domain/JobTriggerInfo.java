package com.hcentive.cloudmanage.domain;

public class JobTriggerInfo {
	
	private String costCenter;
	
	private String instanceId;
	
	private String type;
	
	private String cronExpression;

	public JobTriggerInfo(String costCenter, String instanceId, String type){
		
		this.costCenter = costCenter;
		this.instanceId = instanceId;
		this.type = type;
	}
	
	public JobTriggerInfo(){
		
	}
	
	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
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
	
	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "JobTriggerInfo [costCenter=" + costCenter + ", instanceId="
				+ instanceId + ", type=" + type + ", cronExpression="
				+ cronExpression + "]";
	}

}
