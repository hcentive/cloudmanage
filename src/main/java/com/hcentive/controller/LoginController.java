package com.hcentive.controller;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.LdapUserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LoginController {	
	
	private static final Logger logger=Logger.getLogger(LoginController.class);  

	@RequestMapping(value = { "/", ""}, method = RequestMethod.GET)
	public ModelAndView showHomePage(HttpServletRequest request,Model model) 
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null && auth.getPrincipal() instanceof UserDetails){
			logger.info("User is authenticated so redirecting to dashboard page.");
			return new ModelAndView("redirect:manage");
		}
		model.addAttribute("time", Calendar.getInstance().getTime().toLocaleString());
	//	model.addAttribute("version", serverParamProperties.get("version"));
		logger.info("User is not authenticated so deleivering login page to user");
		 return new ModelAndView("login", model.asMap());
	}
	
	@RequestMapping(value="/login-error",  method = RequestMethod.GET)
	public String showLoginPage()
	{
		logger.info("User entered wrong credentials so delievering login error page to user");
		return "login-error";
	}
	
	@RequestMapping(value="/logout",  method = RequestMethod.GET)
	public String logout(HttpSession session)
	{
		session.invalidate();
		SecurityContextHolder.getContextHolderStrategy().clearContext();
		logger.info("User have been logged out");
		return "redirect:/";
	}
	@RequestMapping(value="/login",  method = RequestMethod.GET)
	public String login(Model model)
	{
		model.addAttribute("time", Calendar.getInstance().getTime().toLocaleString());
		//model.addAttribute("version", serverParamProperties.get("version"));
		logger.info("login page delievered to user");
		return "login";
	}
	@RequestMapping(value="/manage",  method = RequestMethod.GET)
	public String manage(Model model,HttpSession session)
	{
		//model.addAttribute("time", Calendar.getInstance().getTime().toLocaleString());
		//model.addAttribute("version", serverParamProperties.get("version"));
		logger.info("login page delievered to user");
		
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
			//model.addAttribute("username", userdetail.get("cn"));
			session.setAttribute("username", userdetail.get("cn"));
		}
		else if (SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof User)
		{
			User user=(User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			//model.addAttribute("username", user.getUsername());
			session.setAttribute("username", user.getUsername());
		}
		return "redirect:/manage/dashboard";
	}
}