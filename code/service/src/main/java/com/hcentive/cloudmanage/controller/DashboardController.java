package com.hcentive.cloudmanage.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hcentive.cloudmanage.domain.ResourceMetaData;
import com.hcentive.cloudmanage.service.DashboardService;

@RestController
@RequestMapping("/dashboard")
public class DashboardController {
	
	@Autowired
	private DashboardService dashboardService;
	
	@RequestMapping(value="resourceMetaData",method=RequestMethod.GET)
	public Map<String, ResourceMetaData> get(){
		return dashboardService.getDashboardData();
	}

}
