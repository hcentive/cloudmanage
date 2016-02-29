package com.hcentive.cloudmanage.domain;

import static com.hcentive.cloudmanage.domain.BuildInfoConstants.HOST_NAME;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.ID;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.INITIAL_REVISION_NUMBER;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.JIRA_TICKET;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.JOB_ACTIONS;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.JOB_CAUSES;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.JOB_DISPLAY_NAME;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.JOB_PARAMETERS;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.JOB_PARAMETER_NAME;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.JOB_RESULT;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.JOB_TIMESTAMP;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.JSON_PARAMETER_VALUE;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.SERVER_TYPE;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.SHORT_DESCRIPTION;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.SPACE;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.USER_ID;
import static com.hcentive.cloudmanage.domain.BuildInfoConstants.USER_NAME;

import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BuildInfo {

	private String buildId;
	private String jobName;
	private String jiraId;
	private String stack;
	private String revisionNumber;
	private String hostName;
	private String initiatedBy;
	private Calendar initiatedAt;
	private String logFileLocation;
	private Integer lastSuccessfulBuildID;
	private String result;

	private static final Logger logger = LoggerFactory
			.getLogger(BuildInfo.class.getName());

	public BuildInfo(String source) {
		JSONObject jsonObj = new JSONObject(source);

		setBuildId(jsonObj.getString(ID));

		// Split job name by space.
		String displayName = jsonObj.getString(JOB_DISPLAY_NAME);
		setJobName(displayName.split(SPACE)[0]);

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(jsonObj.getLong(JOB_TIMESTAMP));
		setInitiatedAt(cal);
		logger.debug("Extracting attributes from {}", jsonObj.toString());
		extractAttributes(jsonObj);

		setLogFileLocation(getJobName() + "/" + getBuildId() + ".log");
	}

	// Extractor methods
	private void extractAttributes(JSONObject jsonObj) {
		setStatus(jsonObj.getString(JOB_RESULT));
		JSONArray actions = (JSONArray) jsonObj.get(JOB_ACTIONS);
		for (int i = 0; i < actions.length(); i++) {
			Object actionsObj = actions.get(i);
			if (actionsObj instanceof JSONObject) {
				JSONObject actionsJsonObj = (JSONObject) actionsObj;
				for (String key : actionsJsonObj.keySet()) {
					if (key.equals(JOB_CAUSES)) {
						JSONArray causes = actionsJsonObj.getJSONArray(key);
						for (int j = 0; j < causes.length(); j++) {
							JSONObject causesObj = causes.getJSONObject(j);
							StringBuilder initiatedByStrBldr = new StringBuilder(
									causesObj.getString(SHORT_DESCRIPTION))
									.append(SPACE);
							if (causesObj.has(USER_ID)) {
								initiatedByStrBldr.append(causesObj
										.getString(USER_ID));
							} else if (causesObj.has(USER_NAME)) {
								initiatedByStrBldr.append(causesObj
										.getString(USER_NAME));
							}
							setInitiatedBy(initiatedByStrBldr.toString());
						}
					} else if (key.equalsIgnoreCase(JOB_PARAMETERS)) {
						JSONArray params = actionsJsonObj.getJSONArray(key);
						for (int k = 0; k < params.length(); k++) {
							JSONObject paramsObj = params.getJSONObject(k);
							String name = paramsObj.getString(JOB_PARAMETER_NAME);
							if (name.equalsIgnoreCase(JIRA_TICKET)) {
								setJiraId(paramsObj.getString(JSON_PARAMETER_VALUE));
							} else if (name.equalsIgnoreCase(SERVER_TYPE)) {
								setStack(paramsObj.getString(JSON_PARAMETER_VALUE));
							} else if (name
									.equalsIgnoreCase(INITIAL_REVISION_NUMBER)) {
								setRevisionNumber(paramsObj.getString(JSON_PARAMETER_VALUE));
							} else if (name.equalsIgnoreCase(HOST_NAME)) {
								setHostName(paramsObj.getString(JSON_PARAMETER_VALUE));
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

	public void setStatus(String status) {
		this.result = status;
	}

	public void setBuildId(String buildId) {
		this.buildId = buildId;
	}

	public void setLastSuccessfulBuildID(Integer lastSuccessfulBuildID) {
		this.lastSuccessfulBuildID = lastSuccessfulBuildID;
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

	public String getStatus() {
		return result;
	}

	public String getBuildId() {
		return buildId;
	}

	public Integer getLastSuccessfulBuildID() {
		return lastSuccessfulBuildID;
	}

	// Equals
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((buildId == null) ? 0 : buildId.hashCode());
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
		if (buildId == null) {
			if (other.buildId != null)
				return false;
		} else if (!buildId.equals(other.buildId))
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
				+ ", status=" + result + ", jiraId=" + jiraId + ", stack="
				+ stack + ", revisionNumber=" + revisionNumber + ", hostName="
				+ hostName + ", initiatedBy=" + initiatedBy + ", initiatedAt="
				+ initiatedAt + ", logFileLocation=" + logFileLocation + "]";
	}

	public String toJson() throws JsonProcessingException {
		ObjectMapper mapper = new ObjectMapper();
		String jsonString = mapper.writeValueAsString(this);
		System.out.println("Object as json " + jsonString);
		return jsonString;
	}

}
