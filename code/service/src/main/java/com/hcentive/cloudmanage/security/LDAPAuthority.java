package com.hcentive.cloudmanage.security;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "LDAP_AUTHORITY_MASTER")
public class LDAPAuthority implements Serializable {

	private static final long serialVersionUID = 004L;

	@Id
	@Column(name = "LDAP_AUTH_ID")
	private int ldapAuthId;

	@Column(name = "LDAP_AUTH_NAME")
	private String ldapAuthName;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "AUTHORITY_MAP", joinColumns = @JoinColumn(name = "LDAP_AUTHORITY_ID"), inverseJoinColumns = @JoinColumn(name = "APP_AUTHORITY_ID"))
	private Set<AppAuthority> appAuthority = new HashSet<AppAuthority>();

	public LDAPAuthority() {
	}

	public LDAPAuthority(int ldapAuthId, String ldapAuthName,
			Set<AppAuthority> appAuthority) {
		this.ldapAuthId = ldapAuthId;
		this.ldapAuthName = ldapAuthName;
		this.appAuthority = appAuthority;
	}

	public int getLdapAuthId() {
		return ldapAuthId;
	}

	public void setLdapAuthId(int ldapAuthId) {
		this.ldapAuthId = ldapAuthId;
	}

	public String getLdapAuthName() {
		return ldapAuthName;
	}

	public void setLdapAuthName(String ldapAuthName) {
		this.ldapAuthName = ldapAuthName;
	}

	public Set<AppAuthority> getAppAuthority() {
		return appAuthority;
	}

	public void setAppAuthority(Set<AppAuthority> appAuthority) {
		this.appAuthority = appAuthority;
	}

	@Override
	public String toString() {
		return "LDAPAuthority [ldapAuthId=" + ldapAuthId + ", ldapAuthName="
				+ ldapAuthName + ", appAuthority=" + appAuthority + "]";
	}
}
