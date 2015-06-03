package com.hcentive.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hcentive.dao.GenericDAO;
import com.hcentive.domain.CostcenterRole;
import com.hcentive.domain.GlobalRole;
import com.hcentive.domain.Permission;
import com.hcentive.domain.Role;
import com.hcentive.domain.RolePermissionMapping;
import com.hcentive.domain.User;
import com.hcentive.domain.UserRole;
import com.hcentive.service.ManageAssignRoleService;


@Service("manageAssignRole")
public class ManageAssignRoleServiceImpl implements ManageAssignRoleService
{

	@SuppressWarnings("rawtypes")
	@Autowired
	private GenericDAO genericDAO;
	
	@Override
	public List<GlobalRole> getAllGlobalRole()
	{
		@SuppressWarnings("unchecked")
		List<GlobalRole> roles= genericDAO.findByNamedQuery("role.getGlobalRole");
		for(Role r:roles)
		{
			Map<String, Object> queryParameters = new HashMap<String, Object>();
			queryParameters.put("role", r.getRole());

			@SuppressWarnings("unchecked")
			List<String> results = (List<String>) genericDAO.findByNamedQuery("permission.getPermissionsForRole", queryParameters);
			/*if (null == results || results.isEmpty()) {
				return null;
			}*/
			
			r.setPermissions(results);
		}
		return roles;
	}
	
	@Override
	public List<CostcenterRole> getAllCostcenterRole() {
		@SuppressWarnings("unchecked")
		List<CostcenterRole> roles= genericDAO.findByNamedQuery("role.getCostcenterRole");
		for(Role r:roles)
		{
			
			Map<String, Object> queryParameters = new HashMap<String, Object>();
			queryParameters.put("role", r.getRole());
			int index=r.getRole().indexOf("_")+1;
            r.setRole(r.getRole().substring(index));
			@SuppressWarnings("unchecked")
			List<String> results = (List<String>) genericDAO.findByNamedQuery("permission.getPermissionsForRole", queryParameters);
			/*if (null == results || results.isEmpty()) {
				return null;
			}*/
			
			r.setPermissions(results);
		}
		return roles;
	}
	@Override
	public List<User> getAssignedGlobalRoles() {
		List<User> users= genericDAO.findByNamedQuery("role.userHavingGlobalRole");
		for(User u:users)
		{
			
			Map<String, Object> queryParameters = new HashMap<String, Object>();
			queryParameters.put("username", u.getUsername());
			
			@SuppressWarnings("unchecked")
			List<String> results = (List<String>) genericDAO.findByNamedQuery("role.getGlobalRolesByUsername", queryParameters);
			/*if (null == results || results.isEmpty()) {
				return null;
			}*/
			
			u.setRoles(results);
		}
		return users;
	}
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void submitGlobalRoles(List<GlobalRole> updatedGlobalRoles) throws Exception
	{
		List<Permission> permission=(List<Permission>)genericDAO.findByNamedQuery("permission.allPermissions");
		List<GlobalRole> existingGlobalRole=genericDAO.findByNamedQuery("role.getGlobalRole");
		for(Role updatedRole:updatedGlobalRoles)
		{
			//case- when a new global role created
			if(!(existingGlobalRole.contains(updatedRole)))
			{
				updatedRole.setRoleId(null);
				genericDAO.create(updatedRole);
				genericDAO.getEntityManager().flush();				
				if(updatedRole.getRoleId() == null)
					throw new Exception("Persisted role id is getting null");
				else
				{
					for(String perm:updatedRole.getPermissions())
					{
						RolePermissionMapping rpm=new RolePermissionMapping();
						rpm.setRoleId(updatedRole);
						Permission p=new Permission();p.setPermission(perm);
						int index;
						if(( index=permission.indexOf(p))!=-1)
						{
							p=permission.get(index);
						}
						else
							throw  new Exception("permission does not exist");
						rpm.setPermissionId(p);
						genericDAO.create(rpm);
					}
				}
			}
			else
			{
				if("admin".equals(updatedRole.getRole()))
						continue;
				else
				{
					Map<String, Object> queryParameters = new HashMap<String, Object>();
					queryParameters.put("role", updatedRole.getRole());
					Long roleId =(Long) genericDAO.findObjectByNamedQuery("role.getGlobalRoleIDByRole", queryParameters);				
					ArrayList<String> existingPerm = (ArrayList<String>) genericDAO.findByNamedQuery("permission.getPermissionsForRole", queryParameters);
					ArrayList<String> updatedPerm=(ArrayList<String>) updatedRole.getPermissions();
					if (null == existingPerm || existingPerm.isEmpty()) {
						for(String perm:updatedRole.getPermissions())
						{
							RolePermissionMapping rpm=new RolePermissionMapping();
							rpm.setRoleId(existingGlobalRole.get(existingGlobalRole.indexOf(updatedRole)));
							Permission p=new Permission();p.setPermission(perm);
							int index;
							if(( index=permission.indexOf(p))!=-1)
							{
								p=permission.get(index);
							}
							else
								throw  new Exception("permission does not exist");
							rpm.setPermissionId(p);
							genericDAO.create(rpm);
						}
					}
					else
					{
						ArrayList<String> clonedExistingPerm=(ArrayList<String>) existingPerm.clone();
						ArrayList<String>  clonedUpdatedPerm=(ArrayList<String>) updatedPerm.clone();
						clonedUpdatedPerm.removeAll(clonedExistingPerm);
						for(String perm:clonedUpdatedPerm)
						{
							RolePermissionMapping rpm=new RolePermissionMapping();
							rpm.setRoleId(existingGlobalRole.get(existingGlobalRole.indexOf(updatedRole)));
							Permission p=new Permission();p.setPermission(perm);
							int index;
							if(( index=permission.indexOf(p))!=-1)
							{
								p=permission.get(index);
							}
							else
								throw  new Exception("permission does not exist");
							rpm.setPermissionId(p);
							genericDAO.create(rpm);
						}
						existingPerm.removeAll(updatedPerm);
						for(String perm:existingPerm)
						{
							Permission p=new Permission();p.setPermission(perm);
							int index;
							if(( index=permission.indexOf(p))!=-1)
							{
								p=permission.get(index);
							}
							else
								throw  new Exception("permission does not exist");
							Map<String, Object> queryParam = new HashMap<String, Object>();
							queryParam.put("role", existingGlobalRole.get(existingGlobalRole.indexOf(updatedRole)));
							queryParam.put("perm", p);
							int i=genericDAO.executeUpdateNamedQuery("permission.deleteRolePermissionsMpng", queryParam);
							System.out.println("rpm table entry deleted for id "+i);
						}
					}
					
				}
			}
		}
		//existingGlobalRole.removeAll(updatedGlobalRoles);
		removeExistingGlobalElelemt(existingGlobalRole,updatedGlobalRoles);
		for(Role deleteRole:existingGlobalRole)
		{
			Map<String, Object> queryParam = new HashMap<String, Object>();
			queryParam.put("role", deleteRole);
			List<UserRole> userRoleList=genericDAO.findByNamedQuery("role.userRoleByRole", queryParam);
			genericDAO.deleteAll(userRoleList);
			List<RolePermissionMapping> rolePermList=genericDAO.findByNamedQuery("role.rolePermByRole", queryParam);
			genericDAO.deleteAll(rolePermList);
			genericDAO.delete(deleteRole);
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public void submitCostcenterRoles(List<CostcenterRole> updatedGlobalRoles) throws Exception
	{
		List<Permission> permission=(List<Permission>)genericDAO.findByNamedQuery("permission.allPermissions");
		List<CostcenterRole> existingCostcenterRole=genericDAO.findByNamedQuery("role.getCostcenterRole");
		for(Role updatedRole:updatedGlobalRoles)
		{
			//case- when a new global role created
			updatedRole.setRole("costcenter_"+updatedRole.getRole());
			if(!(existingCostcenterRole.contains(updatedRole)))
			{
				updatedRole.setRoleId(null);
				genericDAO.create(updatedRole);
				genericDAO.getEntityManager().flush();				
				if(updatedRole.getRoleId() == null)
					throw new Exception("Persisted role id is getting null");
				else
				{
					for(String perm:updatedRole.getPermissions())
					{
						RolePermissionMapping rpm=new RolePermissionMapping();
						rpm.setRoleId(updatedRole);
						Permission p=new Permission();p.setPermission(perm);
						int index;
						if(( index=permission.indexOf(p))!=-1)
						{
							p=permission.get(index);
						}
						else
							throw  new Exception("permission does not exist");
						rpm.setPermissionId(p);
						genericDAO.create(rpm);
					}
				}
			}
			else
			{
					Map<String, Object> queryParameters = new HashMap<String, Object>();
					queryParameters.put("role", updatedRole.getRole());
					Long roleId =(Long) genericDAO.findObjectByNamedQuery("role.getCostcenterRoleIDByRole", queryParameters);				
					ArrayList<String> existingPerm = (ArrayList<String>) genericDAO.findByNamedQuery("permission.getPermissionsForRole", queryParameters);
					ArrayList<String> updatedPerm=(ArrayList<String>) updatedRole.getPermissions();
					if (null == existingPerm || existingPerm.isEmpty()) {
						for(String perm:updatedRole.getPermissions())
						{
							RolePermissionMapping rpm=new RolePermissionMapping();
							rpm.setRoleId(existingCostcenterRole.get(existingCostcenterRole.indexOf(updatedRole)));
							Permission p=new Permission();p.setPermission(perm);
							int index;
							if(( index=permission.indexOf(p))!=-1)
							{
								p=permission.get(index);
							}
							else
								throw  new Exception("permission does not exist");
							rpm.setPermissionId(p);
							genericDAO.create(rpm);
						}
					}
					else
					{
						ArrayList<String> clonedExistingPerm=(ArrayList<String>) existingPerm.clone();
						ArrayList<String>  clonedUpdatedPerm=(ArrayList<String>) updatedPerm.clone();
						clonedUpdatedPerm.removeAll(clonedExistingPerm);
						for(String perm:clonedUpdatedPerm)
						{
							RolePermissionMapping rpm=new RolePermissionMapping();
							rpm.setRoleId(existingCostcenterRole.get(existingCostcenterRole.indexOf(updatedRole)));
							Permission p=new Permission();p.setPermission(perm);
							int index;
							if(( index=permission.indexOf(p))!=-1)
							{
								p=permission.get(index);
							}
							else
								throw  new Exception("permission does not exist");
							rpm.setPermissionId(p);
							genericDAO.create(rpm);
						}
						existingPerm.removeAll(updatedPerm);
						for(String perm:existingPerm)
						{
							Permission p=new Permission();p.setPermission(perm);
							int index;
							if(( index=permission.indexOf(p))!=-1)
							{
								p=permission.get(index);
							}
							else
								throw  new Exception("permission does not exist");
							Map<String, Object> queryParam = new HashMap<String, Object>();
							queryParam.put("role", existingCostcenterRole.get(existingCostcenterRole.indexOf(updatedRole)));
							queryParam.put("perm", p);
							int i=genericDAO.executeUpdateNamedQuery("permission.deleteRolePermissionsMpng", queryParam);
							System.out.println("rpm table entry deleted for id "+i);
						}
					}
			}
		}
		//existingGlobalRole.removeAll(updatedGlobalRoles);
		removeExistingCostcenterElelemt(existingCostcenterRole,updatedGlobalRoles);
		for(Role deleteRole:existingCostcenterRole)
		{
			Map<String, Object> queryParam = new HashMap<String, Object>();
			queryParam.put("role", deleteRole);
			List<UserRole> userRoleList=genericDAO.findByNamedQuery("role.userRoleByRole", queryParam);
			genericDAO.deleteAll(userRoleList);
			List<RolePermissionMapping> rolePermList=genericDAO.findByNamedQuery("role.rolePermByRole", queryParam);
			genericDAO.deleteAll(rolePermList);
			genericDAO.delete(deleteRole);
		}
	}
	private void removeExistingGlobalElelemt(List<GlobalRole> existingGlobalRole,List<GlobalRole> updatedGlobalRoles)
	{
		ListIterator<GlobalRole> grIterator=existingGlobalRole.listIterator();
		while(grIterator.hasNext())
		{
			GlobalRole gr=grIterator.next();
			for(Role r:updatedGlobalRoles)
			{
				if(gr.getRole().equals(r.getRole()))
					grIterator.remove();
			}
		}
	}
	private void removeExistingCostcenterElelemt(List<CostcenterRole> existingGlobalRole,List<CostcenterRole> updatedGlobalRoles)
	{
		ListIterator<CostcenterRole> grIterator=existingGlobalRole.listIterator();
		while(grIterator.hasNext())
		{
			CostcenterRole gr=grIterator.next();
			for(Role r:updatedGlobalRoles)
			{
				if(gr.getRole().equals(r.getRole()))
					grIterator.remove();
			}
		}
	}

	

	
	
}
