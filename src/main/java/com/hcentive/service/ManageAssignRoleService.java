package com.hcentive.service;

import java.util.List;

import com.hcentive.domain.CostcenterRole;
import com.hcentive.domain.GlobalRole;
import com.hcentive.domain.Role;
import com.hcentive.domain.User;

public interface ManageAssignRoleService {

	void submitGlobalRoles(List<GlobalRole> updatedGlobalRoles) throws Exception;

	List<GlobalRole> getAllGlobalRole();
	List<CostcenterRole> getAllCostcenterRole();
	List<User> getAssignedGlobalRoles();
	void submitCostcenterRoles(List<CostcenterRole> updatedGlobalRoles) throws Exception;

}
