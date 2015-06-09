package com.hcentive.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * @author Manoj Tailor
 * 
 */
@MappedSuperclass
public class  Obstinate implements Serializable {
	private static final long serialVersionUID = 8115919829211280558L;

	@Column(name = "created_by")
	private String createdBy;
	
	
	@Temporal(TemporalType.DATE)
	@Column(name = "created_date", updatable = false)
	private Date createdDate;

	@Column(name = "updated_by")
	
	private String updatedBy;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "updated_date")
	private Date updatedDate;

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getUpdatedDate() {
		return updatedDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public void setUpdatedDate(Date updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	
	//Method to create a complete copy of object.
	//Cloned objects clean up is required if new object needs to be persisted. 
	/*public Object deepClone() throws Exception {
		return UtilityFunctions.deepClone(this);
	}*/
}
