package com.hcentive.cloudmanage.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class Tag implements Serializable {

	private static final long serialVersionUID = 005L;

	@Column(name = "TAG_TYPE")
	private String tagType;

	@Column(name = "TAG_VALUE")
	private String tagValue;

	public Tag() {
	}

	public Tag(String tagType, String tagValue) {
		this.tagType = tagType;
		this.tagValue = tagValue;
	}

	public String getTagType() {
		return tagType;
	}

	public void setTagType(String tagType) {
		this.tagType = tagType;
	}

	public String getTagValue() {
		return tagValue;
	}

	public void setTagValue(String tagValue) {
		this.tagValue = tagValue;
	}

	@Override
	public String toString() {
		return "Tag [tagType=" + tagType + ", tagValue=" + tagValue + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tagType == null) ? 0 : tagType.hashCode());
		result = prime * result
				+ ((tagValue == null) ? 0 : tagValue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tag other = (Tag) obj;
		if (tagType == null) {
			if (other.tagType != null)
				return false;
		} else if (!tagType.equals(other.tagType))
			return false;
		if (tagValue == null) {
			if (other.tagValue != null)
				return false;
		} else if (!tagValue.equals(other.tagValue))
			return false;
		return true;
	}

}