package com.hcentive.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
}
