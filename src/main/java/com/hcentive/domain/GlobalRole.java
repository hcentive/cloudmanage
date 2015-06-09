package com.hcentive.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;

@Entity
@Table(name="globalrole")
@PrimaryKeyJoinColumn(name="role_id")
@NamedQueries(value = {
	     @NamedQuery(name = "role.getGlobalRole", query = "SELECT r FROM GlobalRole r"),
	     @NamedQuery(name = "role.getCostcenterRole", query = "SELECT r FROM CostcenterRole r"),
	     @NamedQuery(name = "role.getGlobalRoleIDByRole", query = "SELECT roleId FROM GlobalRole r WHERE r.role=:role"),
	     @NamedQuery(name = "role.getCostcenterRoleIDByRole", query = "SELECT roleId FROM CostcenterRole r WHERE r.role=:role")
	     })
public class GlobalRole extends Role {
	
	private static final long serialVersionUID = 8115919829215698358L;
	//private static final String prefix="global";
	
	 @Column(name = "global_role")
	 private String role;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		//this.role = prefix+"_"+role;
		this.role = role;
	}

	@OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="globalrole_id")
    //@IndexColumn(name="idx")
    private List<CostcenterRole> costCenterRoles;
	
	public List<CostcenterRole> getCostCenterRoles() {
		return costCenterRoles;
	}

	public void setCostCenterRoles(List<CostcenterRole> costCenterRoles) {
		this.costCenterRoles = costCenterRoles;
	}


	public GlobalRole() {
	}
	
	public GlobalRole(String role) {				
		this.role = role;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(!(obj instanceof GlobalRole))
			return false;
		GlobalRole gr=(GlobalRole)obj;
		return this.role.equals(gr.getRole());
	}
	
}
