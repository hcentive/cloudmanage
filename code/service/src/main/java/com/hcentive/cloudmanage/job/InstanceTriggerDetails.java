package com.hcentive.cloudmanage.job;

import java.util.Date;

public class InstanceTriggerDetails {
	
	private String cron;
	
	private Date previousFireTime;
	
	private Date nextFireTime;

	public String getCron() {
		return cron;
	}

	public void setCron(String cron) {
		this.cron = cron;
	}

	public Date getPreviousFireTime() {
		return previousFireTime;
	}

	public void setPreviousFireTime(Date previousFireTime) {
		this.previousFireTime = previousFireTime;
	}

	public Date getNextFireTime() {
		return nextFireTime;
	}

	public void setNextFireTime(Date nextFireTime) {
		this.nextFireTime = nextFireTime;
	}
	
	
}
