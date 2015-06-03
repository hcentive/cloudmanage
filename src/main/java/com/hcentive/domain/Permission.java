package com.hcentive.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "permission")
@NamedQueries(value = {
		  @NamedQuery(name = "permission.getPermissionsForRole", query = "select p.permission from Role r,Permission p, RolePermissionMapping rp WHERE p.id=rp.permissionId AND r.roleId=rp.roleId AND r.role=:role "),
		  @NamedQuery(name = "permission.getPermissions", query = "select p from Role r,Permission p, RolePermissionMapping rp WHERE p.id=rp.permissionId AND r.roleId=rp.roleId AND r.role=:role "),
		  @NamedQuery(name = "permission.allPermissions", query = "select p from Permission p"),
		  @NamedQuery(name= "role.rolePermByRole", query="SELECT rpm FROM RolePermissionMapping rpm WHERE rpm.roleId=:role")
		 })
public class Permission {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "PERMISSION_ID")
	private Long id;
	 
	@Column(name = "permission")
	 private String permission;
	 public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(!(obj instanceof Permission))
			return false;
		Permission gr=(Permission)obj;
		return this.permission.equals(gr.permission);
	}

	
	 
}
