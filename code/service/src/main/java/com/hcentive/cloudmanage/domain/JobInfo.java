package com.hcentive.cloudmanage.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JobInfo {

	private String description;
	private SuccessfulBuild lastSuccessfulBuild;
	private List<Builds> builds;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public SuccessfulBuild getLastSuccessfulBuild() {
		return lastSuccessfulBuild;
	}

	public void setLastSuccessfulBuild(SuccessfulBuild lastSuccessfulBuild) {
		this.lastSuccessfulBuild = lastSuccessfulBuild;
	}

	public List<Builds> getBuilds() {
		return builds;
	}

	public void setBuilds(List<Builds> builds) {
		this.builds = builds;
	}

	public Integer getLastSuccessfulBuildNumber() {
		if (lastSuccessfulBuild != null)
			return lastSuccessfulBuild.number;
		return null;
	}

	public class SuccessfulBuild {
		private Integer number;

		public Integer getNumber() {
			return number;
		}

		public void setNumber(Integer number) {
			this.number = number;
		}

		@Override
		public String toString() {
			return "SuccessfulBuild [number=" + number + "]";
		}
	}

	public static class Builds {
		private Integer number;

		public Integer getNumber() {
			return number;
		}

		public void setNumber(Integer number) {
			this.number = number;
		}

		@Override
		public String toString() {
			return "Builds [number=" + number + "]";
		}
	}

	@Override
	public String toString() {
		return "JobInfo [description=" + description + ", lastSuccessfulBuild="
				+ lastSuccessfulBuild + ", builds=" + builds + "]";
	}
}
