package com.hcentive.cloudmanage.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcentive.cloudmanage.domain.UserProfile;
import com.hcentive.cloudmanage.service.provider.aws.UserProfileService;

@RestController
@RequestMapping(value="/user")
public class UserController {
	
	@Autowired
	private UserProfileService userProfileService;

	@RequestMapping(method=RequestMethod.GET)
	public Principal user(Principal user) {
		return user;
	}
	
	@RequestMapping(value="/profile", method=RequestMethod.GET)
	public UserProfile getUserProfile(){
		UserProfile userProfile = userProfileService.getUserProfile();
		return userProfile;
	}

}
