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

	@Column(name = "PRODUCT")
	private String product;

	@Column(name = "CLIENT")
	private String client;

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

	@Column(name = "DNS_NAME")
	private String dnsName;

	public AWSMetaInfo() {
	}

	public AWSMetaInfo(String awsInstanceId, String name, String product,
			String client, String costCenter, String stack, String owner,
			String privateIP, String publicIP, String instanceType,
			Date launchTime, String dnsName) {
		this.awsInstanceId = awsInstanceId;
		this.name = name;
		this.product = product;
		this.client = client;
		this.costCenter = costCenter;
		this.stack = stack;
		this.owner = owner;
		this.privateIP = privateIP;
		this.publicIP = publicIP;
		this.instanceType = instanceType;
		this.launchTime = launchTime;
		this.dnsName = dnsName;
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

	public String getProduct() {
		return product;
	}

	public void setProduct(String product) {
		this.product = product;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
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

	public String getDnsName() {
		return dnsName;
	}

	public void setDnsName(String dnsName) {
		this.dnsName = dnsName;
	}

	@Override
	public String toString() {
		return "AWSMetaInfo [id=" + id + ", awsInstanceId=" + awsInstanceId
				+ ", name=" + name + ", product=" + product + ", client="
				+ client + ", costCenter=" + costCenter + ", stack=" + stack
				+ ", owner=" + owner + ", privateIP=" + privateIP
				+ ", publicIP=" + publicIP + ", instanceType=" + instanceType
				+ ", launchTime=" + launchTime + ", dnsName=" + dnsName + "]";
	}

}
