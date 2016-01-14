package com.hcentive.cloudmanage.domain;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;

public class BuildInfo {

	private int buildId;
	private String jobName;
	private String jiraId;
	private String stack;
	private String revisionNumber;
	private String hostName;
	private String initiatedBy;
	private Calendar initiatedAt;
	private String logFileLocation;

	public BuildInfo(String jsonResponse) {
		JSONObject jsonObj = new JSONObject(jsonResponse);
		System.out.println("Received " + jsonObj);

		setBuildId(jsonObj.getInt("id"));

		// Split job name by space.
		String displayName = jsonObj.getString("fullDisplayName");
		setJobName(displayName.split(" ")[0]);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(jsonObj.getLong("timestamp"));
		setInitiatedAt(cal);

		extractAttributes(jsonObj);

		setLogFileLocation(getJobName() + "/" + getBuildId() + ".log");
	}

	// Extractor methods
	private void extractAttributes(JSONObject jsonObj) {
		JSONArray actions = (JSONArray) jsonObj.get("actions");
		for (int i = 0; i < actions.length(); i++) {
			Object actionsObj = actions.get(i);
			if (actionsObj instanceof JSONObject) {
				JSONObject actionsJsonObj = (JSONObject) actionsObj;
				for (String key : actionsJsonObj.keySet()) {
					if (key.equals("causes")) {
						JSONArray causes = actionsJsonObj.getJSONArray(key);
						for (int j = 0; j < causes.length(); j++) {
							JSONObject causesObj = causes.getJSONObject(j);
							StringBuilder initiatedByStrBldr = new StringBuilder(
									causesObj.getString("shortDescription"));
							if (causesObj.has("userId")) {
								initiatedByStrBldr.append(causesObj
										.getString("userId"));
							}
							if (causesObj.has("userName")) {
								initiatedByStrBldr.append(causesObj
										.getString("userName"));
							}
							setInitiatedBy(initiatedByStrBldr.toString());
						}
					} else if (key.equals("parameters")) {
						JSONArray params = actionsJsonObj.getJSONArray(key);
						for (int k = 0; k < params.length(); k++) {
							JSONObject paramsObj = params.getJSONObject(k);
							String name = paramsObj.getString("name");
							if (name.equals("Jira_Ticket")) {
								setJiraId(paramsObj.getString("value"));
							} else if (name.equals("Server_Type")) {
								setStack(paramsObj.getString("value"));
							} else if (name.equals("Initial_Revision_Number")) {
								setRevisionNumber(paramsObj.getString("value"));
							} else if (name.equals("Host_Name")) {
								setHostName(paramsObj.getString("value"));
							}
						}

					}
				}
			}
		}
	}

	// Setters
	public void setJiraId(String jiraId) {
		this.jiraId = jiraId;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public void setRevisionNumber(String revisionNumber) {
		this.revisionNumber = revisionNumber;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public void setInitiatedBy(String initiatedBy) {
		this.initiatedBy = initiatedBy;
	}

	public void setInitiatedAt(Calendar initiatedAt) {
		this.initiatedAt = initiatedAt;
	}

	public void setLogFileLocation(String logFileLocation) {
		this.logFileLocation = logFileLocation;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public void setBuildId(int buildId) {
		this.buildId = buildId;
	}

	// Getters
	public String getJobName() {
		return jobName;
	}

	public String getJiraId() {
		return jiraId;
	}

	public String getStack() {
		return stack;
	}

	public String getRevisionNumber() {
		return revisionNumber;
	}

	public String getHostName() {
		return hostName;
	}

	public String getInitiatedBy() {
		return initiatedBy;
	}

	public Calendar getInitiatedAt() {
		return initiatedAt;
	}

	public String getLogFileLocation() {
		return logFileLocation;
	}

	public int getBuildId() {
		return buildId;
	}

	// Equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + buildId;
		result = prime * result + ((jobName == null) ? 0 : jobName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildInfo other = (BuildInfo) obj;
		if (buildId != other.buildId)
			return false;
		if (jobName == null) {
			if (other.jobName != null)
				return false;
		} else if (!jobName.equals(other.jobName))
			return false;
		return true;
	}

	// ToString
	@Override
	public String toString() {
		return "BuildInfo [buildId=" + buildId + ", jobName=" + jobName
				+ ", jiraId=" + jiraId + ", stack=" + stack
				+ ", revisionNumber=" + revisionNumber + ", hostName="
				+ hostName + ", initiatedBy=" + initiatedBy + ", initiatedAt="
				+ initiatedAt + ", logFileLocation=" + logFileLocation + "]";
	}
}
