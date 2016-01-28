package com.hcentive.cloudmanage.jenkins;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class JobHostKey implements Serializable {

	private static final long serialVersionUID = 003L;

	@Column(name = "JOB_NAME")
	private String jobName;

	@Column(name = "HOST_NAME")
	private String hostName;

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
	public String toString() {
		return "JobHostKey [jobName=" + jobName + ", hostName=" + hostName
				+ "]";
	}
}