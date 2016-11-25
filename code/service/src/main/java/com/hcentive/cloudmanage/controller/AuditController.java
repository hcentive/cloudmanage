package com.hcentive.cloudmanage.controller;

import com.hcentive.cloudmanage.audit.AuditEntity;
import com.hcentive.cloudmanage.audit.AuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(basePath = "/audit",
		value = "audit",
		description = "Api endpoint related to Audit",
		produces = "application/json",
		tags = "audit")
@RestController
@RequestMapping("/audit")
public class AuditController {

	@Autowired
	private AuditService auditService;

	@ApiOperation(value = "Get all audit history",nickname = "Get all audit history")
	@RequestMapping(value = "list",method = RequestMethod.GET)
	public List<AuditEntity> getAuditList() {
		List<AuditEntity> auditEntities = auditService.getAuditsList();
		return auditEntities;
	}

	@ApiOperation(value = "Get audit history by instance id",nickname = "Get audit history by instance id")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "instanceId",value = "Instance id",dataType = "string",required = true,paramType = "path")
	})
	@RequestMapping(value = "list/{instanceId}",method = RequestMethod.GET)
	public List<AuditEntity> getAuditListForInstance(
			@PathVariable(value = "instanceId") String instanceId) {
		List<AuditEntity> auditEntities = auditService
				.getAuditsList(instanceId);
		return auditEntities;
	}

	@ApiOperation(value = "Get latest audit history by instance id",nickname = "Get latest audit history by instance id")
	@RequestMapping(value = "list/latest/{instanceId}",method = RequestMethod.GET)
	public List<AuditEntity> getLatestAuditListForInstance(
			@PathVariable(value = "instanceId") String instanceId) {
		List<AuditEntity> auditEntities = auditService
				.getLatestDistinctAuditsList(instanceId);
		return auditEntities;
	}
}
