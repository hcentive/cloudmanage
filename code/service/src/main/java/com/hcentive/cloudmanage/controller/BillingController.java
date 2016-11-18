package com.hcentive.cloudmanage.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.SystemEnvironmentPropertySource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.hcentive.cloudmanage.billing.BillingInfo;
import com.hcentive.cloudmanage.billing.BillingService;

@RestController
@RequestMapping("/billing")
public class BillingController {
	
	@Autowired
	private BillingService billingService;
	
	@RequestMapping(method=RequestMethod.GET)
	public List<BillingInfo> getBillingInfo(@RequestParam(required=true, name="from") Date fromTime, @RequestParam(required=false, name="to") Date tillTime){
		 List<BillingInfo> billingInfo = billingService.getBilling(fromTime, tillTime);
		return billingInfo;
	}

	@RequestMapping(value = "/cost",method = RequestMethod.GET)
	public BigDecimal getInstanceBillingCost(
			@RequestParam(required = true,name = "from") Date fromDate,
			@RequestParam(required = true,name = "to") Date toDate,
			@RequestParam(required = true,name = "instanceId") String instanceId){
		return billingService.getBillingCost(instanceId,fromDate,toDate);
	}
}
