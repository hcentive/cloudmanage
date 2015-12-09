package com.hcentive.cloudmanage.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "AUTHORITY_TAG_MAP")
public class DecisionMapper implements Serializable {

	private static final long serialVersionUID = 003L;

	@EmbeddedId
	private Tag tag;

	@Column(name = "LDAP_AUTH_NAMES")
	private String ldapAuthNames;

	public DecisionMapper() {
	}

	public DecisionMapper(Tag tag, String ldapAuthNames) {
		this.tag = tag;
		this.ldapAuthNames = ldapAuthNames;
	}

	public Tag getTag() {
		return tag;
	}

	public void setTag(Tag tag) {
		this.tag = tag;
	}

	public String getLdapAuthNames() {
		return ldapAuthNames;
	}

	public void setLdapAuthNames(String ldapAuthNames) {
		this.ldapAuthNames = ldapAuthNames;
	}

	@Override
	public String toString() {
		return "DecisionMapper [tag=" + tag + ", ldapAuthNames="
				+ ldapAuthNames + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((tag == null) ? 0 : tag.hashCode());
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
		DecisionMapper other = (DecisionMapper) obj;
		if (tag == null) {
			if (other.tag != null)
				return false;
		} else if (!tag.equals(other.tag))
			return false;
		return true;
	}
}
