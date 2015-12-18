package com.hcentive.cloudmanage.job;


public class InstanceJobDetails {
	
	private InstanceTriggerDetails start;
	
	private InstanceTriggerDetails stop;

	public InstanceTriggerDetails getStart() {
		return start;
	}

	public InstanceTriggerDetails getStop() {
		return stop;
	}

	public void setStart(InstanceTriggerDetails start) {
		this.start = start;
	}

	public void setStop(InstanceTriggerDetails stop) {
		this.stop = stop;
	}
	
	

}
