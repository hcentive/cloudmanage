package com.hcentive.cloudmanage.jenkins;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "HOST_MASTER")
public class HostMetaInfo implements Serializable {

	private static final long serialVersionUID = 007L;

	@Id
	@Column(name = "HOST_NAME_ID")
	@GeneratedValue
	private Long id;

	@Column(name = "HOST_NAME", unique = true)
	private String hostName;

	public HostMetaInfo() {
	}

	public HostMetaInfo(String hostName) {
		this.hostName = hostName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}


	@Override
	// Only consider hostName
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((hostName == null) ? 0 : hostName.hashCode());
		return result;
	}

	@Override
	// Only consider hostName
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HostMetaInfo other = (HostMetaInfo) obj;
		if (hostName == null) {
			if (other.hostName != null)
				return false;
		} else if (!hostName.equals(other.hostName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "HostMetaInfo [id=" + id + ", hostName=" + hostName + "]";
	}

}
