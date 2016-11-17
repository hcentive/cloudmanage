package com.hcentive.cloudmanage.audit;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "AUDIT_INFO")
public class AuditEntity implements Serializable {

	private static final long serialVersionUID = 002L;

	@Id
	@GeneratedValue
	@Column(name = "AUDIT_ID")
	private long auditId;

	@Column(name = "EVENT_TYPE")
	private String eventType;

	@Column(name = "EVENT_PARAMS")
	private String args;

	@Column(name = "USER")
	private String userName;

	@Column(name = "EVENT_TIME")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar eventTime = Calendar.getInstance();

	public AuditEntity() {
	}

	public AuditEntity(String eventType, String args, String userName) {
		this.eventType = eventType;
		this.args = args;
		this.userName = userName;
	}

	public long getAuditId() {
		return auditId;
	}

	public void setAuditId(long auditId) {
		this.auditId = auditId;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public String getArgs() {
		return args;
	}

	public void setArgs(String args) {
		this.args = args;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Calendar getEventTime() {
		return eventTime;
	}

	public void setEventTime(Calendar eventTime) {
		this.eventTime = eventTime;
	}

//	@Override
//	public int hashCode() {
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((args == null) ? 0 : args.hashCode());
//		result = prime * result + auditId;
//		result = prime * result
//				+ ((eventTime == null) ? 0 : eventTime.hashCode());
//		result = prime * result
//				+ ((eventType == null) ? 0 : eventType.hashCode());
//		result = prime * result
//				+ ((userName == null) ? 0 : userName.hashCode());
//		return result;
//	}

//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		AuditEntity other = (AuditEntity) obj;
//		if (args == null) {
//			if (other.args != null)
//				return false;
//		} else if (!args.equals(other.args))
//			return false;
//		if (auditId != other.auditId)
//			return false;
//		if (eventTime == null) {
//			if (other.eventTime != null)
//				return false;
//		} else if (!eventTime.equals(other.eventTime))
//			return false;
//		if (eventType == null) {
//			if (other.eventType != null)
//				return false;
//		} else if (!eventType.equals(other.eventType))
//			return false;
//		if (userName == null) {
//			if (other.userName != null)
//				return false;
//		} else if (!userName.equals(other.userName))
//			return false;
//		return true;
//	}

	@Override
	public String toString() {
		return "AuditEntity [auditId=" + auditId + ", eventType=" + eventType
				+ ", args=" + args + ", userName=" + userName + ", eventTime="
				+ eventTime + "]";
	}

}
