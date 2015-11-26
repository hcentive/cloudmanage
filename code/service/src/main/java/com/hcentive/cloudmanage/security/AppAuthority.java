package com.hcentive.cloudmanage.security;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "APP_AUTHORITY_MASTER")
public class AppAuthority implements GrantedAuthority {

	private static final long serialVersionUID = 007L;

	@Id
	@Column(name = "APP_AUTH_ID")
	private int appAuthId;

	@Column(name = "APP_AUTH_NAME")
	private String appAuthName;


	public AppAuthority() {
	}

	public AppAuthority(int appAuthId, String appAuthName) {
		this.appAuthId = appAuthId;
		this.appAuthName = appAuthName;
	}

	public int getAppAuthId() {
		return appAuthId;
	}

	public void setAppAuthId(int appAuthId) {
		this.appAuthId = appAuthId;
	}

	public String getAppAuthName() {
		return appAuthName;
	}

	public void setAppAuthName(String appAuthName) {
		this.appAuthName = appAuthName;
	}
	
	@Override
	public String toString() {
		return "AppAuthority [appAuthId=" + appAuthId + ", appAuthName="
				+ appAuthName + "]";
	}

	@Override
	public String getAuthority() {
		return getAppAuthName();
	}
}
