package com.hcentive.controller;

import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.hcentive.domain.CostcenterRole;
import com.hcentive.domain.CostcenterRoleForm;
import com.hcentive.domain.GlobalRole;
import com.hcentive.domain.GlobalRoleForm;
import com.hcentive.domain.Role;
import com.hcentive.domain.User;
import com.hcentive.domain.UserRoleForm;
import com.hcentive.service.ManageAssignRoleService;
import com.hcentive.service.ManageAssignRoleServiceImpl;

@Controller
@RequestMapping("/roles")
public class ManageAssignRoleController 
{
	@Autowired
	private ManageAssignRoleService manageAssignRole;
	

	
	@RequestMapping(value = "/showglobalrole",method = RequestMethod.GET)
	public ModelAndView getAllGlobalRoles(Model model,HttpSession session)
	{
		List<GlobalRole> globalRoles= manageAssignRole.getAllGlobalRole();
		GlobalRoleForm roleForm=new GlobalRoleForm();
		roleForm.setRoles(globalRoles);
		model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("roleForm", roleForm);
		return new ModelAndView("manage_global_role", model.asMap());
	}
	@RequestMapping(value = "/showcostcenterrole",method = RequestMethod.GET)
	public ModelAndView getAllCostcenterRoles(Model model,HttpSession session)
	{
		List<CostcenterRole> costcenterRoles= manageAssignRole.getAllCostcenterRole();
		CostcenterRoleForm roleForm=new CostcenterRoleForm();
		roleForm.setRoles(costcenterRoles);
		model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("roleForm", roleForm);
		return new ModelAndView("manage_costcenter_role", model.asMap());
	}
	@RequestMapping(value = "/showassignedglobalrrole",method = RequestMethod.GET)
	public ModelAndView getAssignedGlobalRoles(Model model,HttpSession session)
	{
		List<User> globalRoleUser= manageAssignRole.getAssignedGlobalRoles();
		UserRoleForm userForm=new UserRoleForm();
		userForm.setUsers(globalRoleUser);
		List<GlobalRole> globalRoles=manageAssignRole.getAllGlobalRole();
		model.addAttribute("username", SecurityContextHolder.getContext().getAuthentication().getName());
		model.addAttribute("userForm", userForm);
		model.addAttribute("globalRoles", globalRoles);
		return new ModelAndView("assign_global_role", model.asMap());
	}
	@RequestMapping(value = "/submitglobalrole",method = RequestMethod.POST) 
	public ModelAndView submitGlobalRoles(@ModelAttribute("roleForm") GlobalRoleForm roleForm) throws Exception
	{
		List<GlobalRole> submittedRoleList=roleForm.getRoles();
		ListIterator<GlobalRole> roleListIterator=submittedRoleList.listIterator();
		while(roleListIterator.hasNext())
		{
			Role r=roleListIterator.next();
			if(r.getRole() == null)
				roleListIterator.remove();
		}
		try {
			manageAssignRole.submitGlobalRoles(submittedRoleList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return new ModelAndView("redirect:/manage/dashboard");
	}
	@RequestMapping(value = "/submitcostcenterrole",method = RequestMethod.POST) 
	public ModelAndView submitCostcenterRoles(@ModelAttribute("roleForm") CostcenterRoleForm roleForm) throws Exception
	{
		List<CostcenterRole> submittedRoleList=roleForm.getRoles();
		ListIterator<CostcenterRole> roleListIterator=submittedRoleList.listIterator();
		while(roleListIterator.hasNext())
		{
			Role r=roleListIterator.next();
			if(r.getRole() == null)
				roleListIterator.remove();
		}
		try {
			manageAssignRole.submitCostcenterRoles(submittedRoleList);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
		return new ModelAndView("redirect:/manage/dashboard");
	}
}
