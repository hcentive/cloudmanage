package com.hcentive.cloudmanage.domain;

public class JobTriggerInfoDTO {
	
	private StartJobTriggerInfo startJobTriggerInfo;
	
	private StopJobTriggerInfo stopJobTriggerInfo;

	public StartJobTriggerInfo getStartJobTriggerInfo() {
		return startJobTriggerInfo;
	}

	public void setStartJobTriggerInfo(StartJobTriggerInfo startJobTriggerInfo) {
		this.startJobTriggerInfo = startJobTriggerInfo;
	}

	public StopJobTriggerInfo getStopJobTriggerInfo() {
		return stopJobTriggerInfo;
	}

	public void setStopJobTriggerInfo(StopJobTriggerInfo stopJobTriggerInfo) {
		this.stopJobTriggerInfo = stopJobTriggerInfo;
	}
	
	

}
