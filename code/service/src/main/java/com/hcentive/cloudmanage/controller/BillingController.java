package com.hcentive.cloudmanage.controller;

import com.hcentive.cloudmanage.billing.BillingInfo;
import com.hcentive.cloudmanage.billing.BillingService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Api(basePath = "/billing",
		value = "billing",
		description = "Api endpoint related to Instance Billing",
		produces = "application/json",
		tags = "billing")
@RestController
@RequestMapping("/billing")
public class BillingController {
	
	@Autowired
	private BillingService billingService;

	@ApiOperation(value = "Get daily billing snapshot of instances between dates",
			nickname = "Get daily billing snapshot of instances between dates")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "from",value = "mm/dd/yyyy",required = true,dataType = "Date",paramType = "query"),
			@ApiImplicitParam(name = "to",value = "mm/dd/yyyy",required = true,dataType = "Date",paramType = "query")
	})
	@RequestMapping(method=RequestMethod.GET)
	public List<BillingInfo> getBillingInfo(@RequestParam(required=true, name="from") Date fromTime, @RequestParam(required=false, name="to") Date tillTime){
		 List<BillingInfo> billingInfo = billingService.getBilling(fromTime, tillTime);
		return billingInfo;
	}

	@ApiOperation(value = "Get instance billing cost between dates",
		nickname = "Get instance billing cost between dates")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "from", value = "dd/mm/yyyy", required = true, dataType = "Date", paramType = "query"),
			@ApiImplicitParam(name = "to", value = "dd/mm/yyyy", required = true, dataType = "Date", paramType = "query"),
			@ApiImplicitParam(name = "instanceId",value = "Instance id",required = true,dataType = "string",paramType = "query")
	})
	@RequestMapping(value = "/cost",method = RequestMethod.GET)
	public BigDecimal getInstanceBillingCost(
			@RequestParam(required = true,name = "from") Date fromDate,
			@RequestParam(required = true,name = "to") Date toDate,
			@RequestParam(required = true,name = "instanceId") String instanceId){
		return billingService.getBillingCost(instanceId,fromDate,toDate);
	}
}
