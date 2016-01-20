package com.hcentive.cloudmanage.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobInfo {
	
	private String description;
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	private SuccessfulBuild lastSuccessfulBuild;

	public SuccessfulBuild getLastSuccessfulBuild() {
		return lastSuccessfulBuild;
	}

	public void setLastSuccessfulBuild(SuccessfulBuild lastSuccessfulBuild) {
		this.lastSuccessfulBuild = lastSuccessfulBuild;
	}
	
	public Integer getLastSuccessfulBuildNumber(){
		if(lastSuccessfulBuild != null)
			return lastSuccessfulBuild.number;
		return null;
	}

	public class SuccessfulBuild{
		private Integer number;

		public Integer getNumber() {
			return number;
		}

		public void setNumber(Integer number) {
			this.number = number;
		}
		
	}

}
