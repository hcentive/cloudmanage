package com.hcentive.cloudmanage.jenkins;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "BUILD_MASTER")
public class BuildMetaInfo implements Serializable {

	private static final long serialVersionUID = 007L;

	@Id
	@Column(name = "JOB_NAME_ID")
	@GeneratedValue
	private Long id;

	@Column(name = "JOB_NAME", unique = true)
	private String jobName;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "BUILD_HOST_MAP", joinColumns = @JoinColumn(name = "JOB_ID"), inverseJoinColumns = @JoinColumn(name = "HOST_ID"))
	private List<HostMetaInfo> hostMetaInfoList;

	public BuildMetaInfo() {
	}

	public BuildMetaInfo(String jobName) {
		this.jobName = jobName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public List<HostMetaInfo> getHostMetaInfoList() {
		return hostMetaInfoList;
	}

	public void setHostMetaInfoList(List<HostMetaInfo> hostMetaInfoList) {
		this.hostMetaInfoList = hostMetaInfoList;
	}

	@Override
	public String toString() {
		return "BuildMetaInfo [id=" + id + ", jobName=" + jobName
				+ ", hostMetaInfoList=" + hostMetaInfoList + "]";
	}
}
