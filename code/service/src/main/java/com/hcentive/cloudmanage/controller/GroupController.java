package com.hcentive.cloudmanage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcentive.cloudmanage.domain.Group;
import com.hcentive.cloudmanage.service.GroupService;

@RestController
public class GroupController {
	
	@Autowired
	private GroupService groupService;
	
	@RequestMapping(value="/groups", method=RequestMethod.GET)
	public List<String> getGroups(Authentication authentication){
		List<String> groups = groupService.getGroups();
		return groups;
	}

}
