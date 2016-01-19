package com.hcentive.cloudmanage.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BuildJobResponse {
	
	private List<JobMetaInfo> jobs;

	public List<JobMetaInfo> getJobs() {
		return jobs;
	}

	public void setJobs(List<JobMetaInfo> jobs) {
		this.jobs = jobs;
	}


}
