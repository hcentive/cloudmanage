package com.hcentive.cloudmanage.domain;

public class Alarm {
	
	private String name;
	private String instanceId;
	private Double threshold;
	private Integer frequency;
	private boolean isEnable;
	private boolean isAlarmConfigured;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	public Double getThreshold() {
		return threshold;
	}
	public void setThreshold(Double threshold) {
		this.threshold = threshold;
	}
	public Integer getFrequency() {
		return frequency;
	}
	public void setFrequency(Integer frequency) {
		this.frequency = frequency;
	}
	
	public boolean isEnable() {
		return isEnable;
	}
	public void setEnable(boolean isEnable) {
		this.isEnable = isEnable;
	}
	
	public boolean isAlarmConfigured() {
		return isAlarmConfigured;
	}
	public void setAlarmConfigured(boolean isAlarmConfigured) {
		this.isAlarmConfigured = isAlarmConfigured;
	}
	
	@Override
	public String toString() {
		return "Alarm [name=" + name + ", instanceId=" + instanceId + ", threshold=" + threshold + ", frequency="
				+ frequency + ", isEnable=" + isEnable + ", isAlarmConfigured=" + isAlarmConfigured + "]";
	}
}