package com.hcentive.cloudmanage.billing;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "AWS_BILL_FILE_INFO")
public class BillFileInfo implements Serializable {

	private static final long serialVersionUID = 006L;

	@Id
	@Column(name = "ID")
	@GeneratedValue
	private Long id;

	@Column(name = "BILL_FILE")
	private String billFile;

	public BillFileInfo() {
	}

	public BillFileInfo(String billFile) {
		super();
		this.billFile = billFile;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBillFile() {
		return billFile;
	}

	public void setBillFile(String billFile) {
		this.billFile = billFile;
	}

	@Override
	public String toString() {
		return "BillFileInfo [id=" + id + ", billFile=" + billFile + "]";
	}
}
