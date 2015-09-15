package com.hcentive.cloudmanage.controller;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.service.provider.ProviderFacade;

@RestController
@RequestMapping("/instances")
public class InstanceController {
	
	@Autowired
	private ProviderFacade provideFacade;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<Instance> list(@RequestParam(value="group",required=false) List<String> groups) throws AccessDeniedException{
		if(CollectionUtils.isEmpty(groups)){
			return provideFacade.getAllInstanceList();
		}
		return provideFacade.getInstanceList(groups);
	}

}
