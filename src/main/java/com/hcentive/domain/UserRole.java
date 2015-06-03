package com.hcentive.domain;

import javax.persistence.Column;
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
@Table(name = "user_role")
@NamedQueries(value = {
	    // @NamedQuery(name = "role.getRolesByUsername", query = "SELECT r FROM Role r WHERE roleId IN(:roleIdList)")
		@NamedQuery(name = "role.getRolesByUsername", query = "SELECT r FROM UserRole ur, User u, Role r WHERE ur.username.id=u.id AND ur.role.roleId=r.roleId AND u.username=:username"),
		@NamedQuery(name= "role.userRoleByRole", query="SELECT ur FROM UserRole ur WHERE ur.role=:role"),
		@NamedQuery(name= "role.userHavingGlobalRole", query="SELECT u FROM User u,UserRole ur,GlobalRole gr WHERE u.id=ur.username.id AND ur.role.roleId=gr.roleId"),
		@NamedQuery(name = "role.getGlobalRolesByUsername", query = "SELECT r.role FROM UserRole ur, User u, GlobalRole r WHERE ur.username.id=u.id AND ur.role.roleId=r.roleId AND u.username=:username"),
		
	     })
public class UserRole extends Obstinate
{
	private static final long serialVersionUID = 1L;

	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private Long id;
	 
	 @ManyToOne
	 @JoinColumn(name = "USERNAME")
	 private User username;
	 
	 @ManyToOne
	 @JoinColumn(name = "ROLE_ID")
	 private Role role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUsername() {
		return username;
	}

	public void setUsername(User username) {
		this.username = username;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	
}
