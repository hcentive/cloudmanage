package com.hcentive.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "group_identifiers")
public class GroupIdentifiers 
{
	private static final long serialVersionUID = 4726011758044334696L;
	
	   @Id
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private int id;
	private String groupId;
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	private String groupName;
}
