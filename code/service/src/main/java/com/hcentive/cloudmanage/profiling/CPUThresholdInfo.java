package com.hcentive.cloudmanage.profiling;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.hcentive.cloudmanage.billing.AWSMetaInfo;

@Entity
@Table(name = "AWS_CPU_THRESHOLD")
public class CPUThresholdInfo implements Serializable {

	private static final long serialVersionUID = 004L;

	@Id
	@Column(name = "THRESHOLD_META_ID")
	@GeneratedValue
	private Long id;

	@Column(name = "THRESHOLD_CPU_DAILY")
	private int dailyCPUThreshold;

	@Column(name = "SKIP_ME_FLAG")
	private boolean skipMeFlag;

	@ManyToOne(cascade = CascadeType.MERGE)
	@JoinColumn(name = "INSTANCE_ID", nullable = false)
	private AWSMetaInfo instanceInfo;

	public CPUThresholdInfo() {
	}

	public CPUThresholdInfo(int dailyCPUThreshold, boolean skipMeFlag,
			AWSMetaInfo instanceInfo) {
		super();
		this.dailyCPUThreshold = dailyCPUThreshold;
		this.skipMeFlag = skipMeFlag;
		this.instanceInfo = instanceInfo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDailyCPUThreshold() {
		return dailyCPUThreshold;
	}

	public void setDailyCPUThreshold(int dailyCPUThreshold) {
		this.dailyCPUThreshold = dailyCPUThreshold;
	}

	public boolean isSkipMeFlag() {
		return skipMeFlag;
	}

	public void setSkipMeFlag(boolean skipMeFlag) {
		this.skipMeFlag = skipMeFlag;
	}

	public AWSMetaInfo getInstanceInfo() {
		return instanceInfo;
	}

	public void setInstanceInfo(AWSMetaInfo instanceInfo) {
		this.instanceInfo = instanceInfo;
	}

	@Override
	public String toString() {
		return "CPUThresholdInfo [id=" + id + ", dailyCPUThreshold="
				+ dailyCPUThreshold + ", skipMeFlag=" + skipMeFlag
				+ ", instanceInfo=" + instanceInfo + "]";
	}

}
