package com.hcentive.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="costcenterrole")
@PrimaryKeyJoinColumn(name="ROLE_ID")
public class CostcenterRole extends Role {

	private static final long serialVersionUID = 8189356829211280558L;
	//private static final String prefix="costcenter";
	
	@Column(name = "costcenter_role")
	 private String role;

	public String getRole() {
		return  role;
	}

	public void setRole(String role) {
		this.role =role;
	}
	
	
	@ManyToOne
    @JoinColumn(name="globalrole_id",insertable=false, updatable=false, 
            nullable=true)
    private GlobalRole globalRole;
	
	

	public GlobalRole getGlobalRole() {
		return globalRole;
	}

	public void setGlobalRole(GlobalRole globalRole) {
		this.globalRole = globalRole;
	}

	public CostcenterRole() {
	}
	
	public CostcenterRole(String role) {
		
		this.role=role;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		if(!(obj instanceof CostcenterRole))
			return false;
		CostcenterRole gr=(CostcenterRole)obj;
		return this.role.equals(gr.getRole());
	}
	
}
