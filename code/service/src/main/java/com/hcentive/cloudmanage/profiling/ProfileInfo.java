package com.hcentive.cloudmanage.profiling;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.hcentive.cloudmanage.billing.AWSMetaInfo;

@Entity
@Table(name = "AWS_CPU_INFO")
public class ProfileInfo implements Serializable {

	private static final long serialVersionUID = 006L;

	@Id
	@Column(name = "META_INFO_ID")
	@GeneratedValue
	private Long id;

	@Column(name = "AVG_CPU_HOURLY")
	private String avgCPUHourly;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SNAPSHOT_AT")
	private Date snapshotAt;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "INSTANCE_ID", nullable = false)
	private AWSMetaInfo instanceInfo;

	public ProfileInfo() {
	}

	public ProfileInfo(String avgCPUHourly, Date snapshotAt,
			AWSMetaInfo instanceInfo) {
		this.avgCPUHourly = avgCPUHourly;
		this.snapshotAt = snapshotAt;
		this.instanceInfo = instanceInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getSnapshotAt() {
		return snapshotAt;
	}

	public void setSnapshotAt(Date snapshotAt) {
		this.snapshotAt = snapshotAt;
	}

	public String getAvgCPUHourly() {
		return avgCPUHourly;
	}

	public void setAvgCPUHourly(String avgCPUHourly) {
		this.avgCPUHourly = avgCPUHourly;
	}

	public AWSMetaInfo getInstanceInfo() {
		return instanceInfo;
	}

	public void setInstanceInfo(AWSMetaInfo instanceInfo) {
		this.instanceInfo = instanceInfo;
	}

	@Override
	public String toString() {
		return "ProfileInfo [id=" + id + ", avgCPUHourly=" + avgCPUHourly
				+ ", snapshotAt=" + snapshotAt + ", instanceInfo="
				+ instanceInfo + "]";
	}

}
