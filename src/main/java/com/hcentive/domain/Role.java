package com.hcentive.domain;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@Table(name = "role")
@Inheritance(strategy=InheritanceType.JOINED)
/*@NamedQueries(value = {
	     //@NamedQuery(name = "role.getGlobalRole", query = "SELECT r FROM GlobalRole r"),
	     @NamedQuery(name = "role.getGlobalRoleByRole", query = "SELECT r.roleId FROM Role r WHERE r.role:role")
	     })*/
public class Role extends Obstinate{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6564760131127775333L;

	@Id
	@GeneratedValue
	@Column(name = "ROLE_ID")
	private Long roleId;
	
	@Column(name = "role")
	private String role;
	
	@Transient
	private List<String> permissions;

	public List<String> getPermissions() {
		return permissions;
	}


	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}


	public Role() {
	
	}
	
    
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}


	public String getRole() {
		return role;
	}


	public void setRole(String role) {
		this.role = role;
	}
	
}
