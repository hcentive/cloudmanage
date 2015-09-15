package com.hcentive.cloudmanage.domain;

public class Group {

	public Group(String value, String displayValue) {
		this.value = value;
		this.displayValue = displayValue;
	}


	private String value;
	private String displayValue;

	public String getDisplayValue() {
		return displayValue;
	}

	public void setDisplayValue(String displayValue) {
		this.displayValue = displayValue;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
