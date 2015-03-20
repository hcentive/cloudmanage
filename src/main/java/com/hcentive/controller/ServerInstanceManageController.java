package com.hcentive.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hcentive.domain.ServerInstance;
import com.hcentive.service.ServerInstanceManageServiceImpl;

@RequestMapping("/manage")
@Controller
public class ServerInstanceManageController 
{
	@Autowired
	ServerInstanceManageServiceImpl manageService;
	

	@RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public ModelAndView getDashboardPage(Model model,HttpSession session) 
	{
		model.addAttribute("time", Calendar.getInstance().getTime().toLocaleString());
		model.addAttribute("username",getUserName(session));
		model.addAttribute("instanceList",manageService.renderDashboardPage());
		return new ModelAndView("manage", model.asMap());
	}
	@RequestMapping(value = "/refreshDashboard",method = RequestMethod.GET)
	public @ResponseBody List<ServerInstance> getAllServerDetails()
	{
		return manageService.renderDashboardPage();
	}
	@RequestMapping(value = "/startServerInstance", method = RequestMethod.POST)
	public @ResponseBody String startServerInstance(@RequestParam String instanceId) 
	{
		
		return manageService.startServerInstance(instanceId);
	}
	@RequestMapping(value = "/stopServerInstance", method = RequestMethod.POST)
	public @ResponseBody String stopServerInstance(@RequestParam String instanceId)
	{
		
		return manageService.stopServerInstance(instanceId);
	}
	@RequestMapping(value = "/terminateServerInstance", method = RequestMethod.POST)
	public @ResponseBody String terminateServerInstance(@RequestParam String instanceId)
	{
		
		return manageService.terminateServerInstance(instanceId);
	}
	@RequestMapping(value = "/refreshServerInstance", method = RequestMethod.POST)
	public @ResponseBody ServerInstance getServerInstanceDetail(@RequestParam String instanceId)
	{
		
		return manageService.refreshServerInstance(instanceId);
	}
	protected String getUserName(HttpSession session)
	{
		if(session.getAttribute("username") == null)
		{
			if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof LdapUserDetails)
			{
				LdapUserDetails ldapUSer = (LdapUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				String[] dn =ldapUSer.getDn().split(",");
				Map<String,String> userdetail=new HashMap<String, String>();
				for(String str:dn)
				{
					String[] string=str.split("=");
					userdetail.put(string[0], string[1]);
				}
				//logger.info("dashboadrd page accessed for user name::"+ userdetail.get("cn"));
				return userdetail.get("cn");
			}
			else if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User)
			{
				User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				//logger.info("dashboadrd page accessed for user name::"+ user.getUsername());
				//logger.info("password have been fetched for user name::"+user.getPassword());
				return user.getUsername();
			}
		}
		else
			return (String)session.getAttribute("username");
		return "";
	}
	}
