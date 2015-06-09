package com.hcentive.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name = "role_permission_mpng")
@NamedQueries(value = {
		
		@NamedQuery(name = "permission.deleteRolePermissionsMpng", query = "delete from RolePermissionMapping rp WHERE rp.permissionId=:perm AND rp.roleId=:role")
	     })
public class RolePermissionMapping extends Obstinate{

	 private static final long serialVersionUID = 1L;
 
 @Id
 @GeneratedValue(strategy = GenerationType.AUTO)
 private Long id;

 @ManyToOne
 @JoinColumn(name = "ROLE_ID")
 private Role roleId;

 @ManyToOne
 @JoinColumn(name = "PERMISSION_ID")
 private Permission permissionId;
 

 
 public Role getRoleId() {
	return roleId;
}

public void setRoleId(Role roleId) {
	this.roleId = roleId;
}

public Permission getPermissionId() {
	return permissionId;
}

public void setPermissionId(Permission permissionId) {
	this.permissionId = permissionId;
}

public Long getId() {
	  return id;
	 }
	 
	 public void setId(Long id) {
	  this.id = id;
	 }



}

