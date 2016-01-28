package com.hcentive.cloudmanage.billing;

import java.io.Serializable;
import java.math.BigInteger;
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

@Entity
@Table(name = "AWS_META_INFO")
public class BillingInfo implements Serializable {

	private static final long serialVersionUID = 006L;

	@Id
	@Column(name = "META_INFO_ID")
	@GeneratedValue
	private Long id;

	@Column(name = "AVG_CPU_HOURLY")
	private String avgCPUBill;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SNAPSHOT_AT")
	private Date snapshotAt;

	@Column(name = "BILL_TOTAL")
	private BigInteger dayTotal;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "INSTANCE_ID", nullable = false)
	private AWSMetaInfo instanceInfo;

	public BillingInfo() {
	}

	public BillingInfo(String avgCPUBill, Date snapshotAt,
			BigInteger dayTotal, AWSMetaInfo instanceInfo) {
		this.avgCPUBill = avgCPUBill;
		this.snapshotAt = snapshotAt;
		this.dayTotal = dayTotal;
		this.instanceInfo = instanceInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAvgCPUBill() {
		return avgCPUBill;
	}

	public void setAvgCPUBill(String avgCPUBill) {
		this.avgCPUBill = avgCPUBill;
	}

	public Date getSnapshotAt() {
		return snapshotAt;
	}

	public void setSnapshotAt(Date snapshotAt) {
		this.snapshotAt = snapshotAt;
	}

	public BigInteger getDayTotal() {
		return dayTotal;
	}

	public void setDayTotal(BigInteger dayTotal) {
		this.dayTotal = dayTotal;
	}

	public AWSMetaInfo getInstanceInfo() {
		return instanceInfo;
	}

	public void setInstanceInfo(AWSMetaInfo instanceInfo) {
		this.instanceInfo = instanceInfo;
	}

	@Override
	public String toString() {
		return "BillingInfo [id=" + id + ", avgCPUBill=" + avgCPUBill
				+ ", snapshotAt=" + snapshotAt + ", dayTotal=" + dayTotal
				+ ", instanceInfo=" + instanceInfo + "]";
	}

}
