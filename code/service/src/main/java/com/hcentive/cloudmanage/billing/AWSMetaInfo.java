package com.hcentive.cloudmanage.billing;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "INSTANCE_MASTER")
public class AWSMetaInfo implements Serializable {

	private static final long serialVersionUID = 004L;

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@Column(name = "AWS_EC2_ID")
	private String awsInstanceId;

	@Column(name = "NAME")
	private String name;

	@Column(name = "PROJECT")
	private String project;

	@Column(name = "COST_CENTER")
	private String costCenter;

	@Column(name = "STACK")
	private String stack;

	@Column(name = "OWNER")
	private String owner;

	@Column(name = "PRIVATE_IP")
	private String privateIP;

	@Column(name = "PUBLIC_IP")
	private String publicIP;

	@Column(name = "INSTANCE_TYPE")
	private String instanceType;

	@Temporal(value = TemporalType.TIMESTAMP)
	@Column(name = "LAUNCH_TIME")
	private Date launchTime;

	public AWSMetaInfo() {
	}

	public AWSMetaInfo(String awsInstanceId, String name, String project,
			String costCenter, String stack, String owner, String privateIP,
			String publicIP, String instanceType, Date launchTime) {
		this.awsInstanceId = awsInstanceId;
		this.name = name;
		this.project = project;
		this.costCenter = costCenter;
		this.stack = stack;
		this.owner = owner;
		this.privateIP = privateIP;
		this.publicIP = publicIP;
		this.instanceType = instanceType;
		this.launchTime = launchTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAwsInstanceId() {
		return awsInstanceId;
	}

	public void setAwsInstanceId(String awsInstanceId) {
		this.awsInstanceId = awsInstanceId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getStack() {
		return stack;
	}

	public void setStack(String stack) {
		this.stack = stack;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getPrivateIP() {
		return privateIP;
	}

	public void setPrivateIP(String privateIP) {
		this.privateIP = privateIP;
	}

	public String getPublicIP() {
		return publicIP;
	}

	public void setPublicIP(String publicIP) {
		this.publicIP = publicIP;
	}

	public String getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}

	public Date getLaunchTime() {
		return launchTime;
	}

	public void setLaunchTime(Date launchTime) {
		this.launchTime = launchTime;
	}

	@Override
	public String toString() {
		return "AWSMetaInfo [id=" + id + ", awsInstanceId=" + awsInstanceId
				+ ", name=" + name + ", project=" + project + ", costCenter="
				+ costCenter + ", stack=" + stack + ", owner=" + owner
				+ ", privateIP=" + privateIP + ", publicIP=" + publicIP
				+ ", instanceType=" + instanceType + ", launchTime="
				+ launchTime + "]";
	}

}
