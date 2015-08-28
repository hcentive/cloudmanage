package com.hcentive.cloudmanage.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vaibhav.gupta
 *
 */
public class VPC {

	private String vpcId;

	private String state;

	private String cidrBlock;

	private String dhcpOptionsId;

	private List<Tag> tags = new ArrayList<Tag>();

	private String instanceTenancy;

	private Boolean isDefault;

	public String getVpcId() {
		return vpcId;
	}

	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCidrBlock() {
		return cidrBlock;
	}

	public void setCidrBlock(String cidrBlock) {
		this.cidrBlock = cidrBlock;
	}

	public String getDhcpOptionsId() {
		return dhcpOptionsId;
	}

	public void setDhcpOptionsId(String dhcpOptionsId) {
		this.dhcpOptionsId = dhcpOptionsId;
	}

	//TODO - Dozer is not populating this
	public List<Tag> getTags() {
		return tags;
	}

	public void setTags(List<Tag> tags) {
		this.tags = tags;
	}

	public String getInstanceTenancy() {
		return instanceTenancy;
	}

	public void setInstanceTenancy(String instanceTenancy) {
		this.instanceTenancy = instanceTenancy;
	}

	public Boolean getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Boolean isDefault) {
		this.isDefault = isDefault;
	}

}
