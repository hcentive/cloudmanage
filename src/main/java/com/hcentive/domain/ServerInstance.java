package com.hcentive.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "server_instance")
public class ServerInstance implements Serializable
{
	private static final long serialVersionUID = 4726011758047365696L;
	
	public static enum ServerInstanceState {
		running, starting, stopped,stopping
	};
	
   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private int id;
   private Date createtime;
   private String name;
   @Enumerated(EnumType.STRING)
   private String state;
   private String owner;
   private String instanceId;
   private String architecture;
   private String imageId;
   private String instanceType;
   private String keyName;
   private String privateIpAddress;
   private String subnetId;
   private String vpcId;
   private String costCenter;
   @OneToMany
   private List<GroupIdentifiers> securityGroups;
   private int maxSecurityGroups=0;
   
   
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	 @Override
	public String toString() {
		// TODO Auto-generated method stub
		return this.name+"   "+this.state+"   "+this.createtime+"    "+this.getOwner();
	}
	public String getArchitecture() {
		return architecture;
	}
	public void setArchitecture(String architecture) {
		this.architecture = architecture;
	}
	public String getImageId() {
		return imageId;
	}
	public void setImageId(String imageId) {
		this.imageId = imageId;
	}
	public String getInstanceType() {
		return instanceType;
	}
	public void setInstanceType(String instanceType) {
		this.instanceType = instanceType;
	}
	public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getPrivateIpAddress() {
		return privateIpAddress;
	}
	public void setPrivateIpAddress(String privateIpAddress) {
		this.privateIpAddress = privateIpAddress;
	}
	public String getSubnetId() {
		return subnetId;
	}
	public void setSubnetId(String subnetId) {
		this.subnetId = subnetId;
	}
	public String getVpcId() {
		return vpcId;
	}
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
	public String getCostCenter() {
		return costCenter;
	}
	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}
	public List<GroupIdentifiers> getSecurityGroups() {
		return securityGroups;
	}
	public void setSecurityGroups(List<GroupIdentifiers> securityGroups) {
		this.securityGroups = securityGroups;
	}
	public int getMaxSecurityGroups() {
		return maxSecurityGroups;
	}
	public void setMaxSecurityGroups(int maxSecurityGroups) {
		this.maxSecurityGroups = maxSecurityGroups;
	}
}
