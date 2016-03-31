package com.hcentive.cloudmanage.jenkins;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Entity
@Table(name = "LAST_SUCCESSFUL_BUILD")
public class LastSuccessfulBuildInfo implements Serializable {

	private static final long serialVersionUID = 004L;

	@EmbeddedId
	private JobHostKey jobHostKey;

	@Column(name = "BUILD_NUMBER")
	private int buildNumber;

	@Column(name = "BUILD_INFO_JSON")
	@JsonIgnore
	private String buildInfoJson;

	public JobHostKey getJobHostKey() {
		return jobHostKey;
	}

	public void setJobHostKey(JobHostKey jobHostKey) {
		this.jobHostKey = jobHostKey;
	}

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	public String getBuildInfoJson() {
		return buildInfoJson;
	}

	public void setBuildInfoJson(String buildInfoJson) {
		this.buildInfoJson = buildInfoJson;
	}
	
	public Map<String, Object> getBuildInfo(){
		Map<String, Object> buildInfo = new HashMap<String, Object>();
		ObjectMapper mapper = new ObjectMapper();
		try {
			buildInfo = mapper.readValue(buildInfoJson, new TypeReference<Map<String, Object>>(){});
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return buildInfo;
		
	}

	@Override
	public String toString() {
		return "LastSuccessfulBuildInfo [jobHostKey=" + jobHostKey
				+ ", buildNumber=" + buildNumber + ", buildInfoJson="
				+ buildInfoJson + "]";
	}

}
