package com.hcentive.cloudmanage.controller;

import com.hcentive.cloudmanage.audit.AuditContextHolder;
import com.hcentive.cloudmanage.audit.AuditEntity;
import com.hcentive.cloudmanage.audit.AuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(basePath = "/audit",
		value = "audit",
		description = "Api endpoint related to Audit",
		produces = "application/json",
		tags = "audits")
@RestController
@RequestMapping("/audits")
public class AuditController {

	@Autowired
	private AuditService auditService;

	@ApiOperation(value = "Get all audit history",nickname = "Get all audit history")
	@ApiImplicitParams({
			@ApiImplicitParam(name = "pageSegment",value = "Page block",defaultValue = "0",required = true,dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "pageSize",value = "No. of records to be retrieved",defaultValue = "50",required = true,dataType = "int",paramType = "query"),
			@ApiImplicitParam(name = "latest",value = "Get record based on recently added",defaultValue = "true",required = true,dataType = "boolean",paramType = "query")
	})
	@RequestMapping(method = RequestMethod.GET)
	public List<AuditEntity> getAudits(@RequestParam Integer pageSegment,@RequestParam Integer pageSize,@RequestParam Boolean latest){
		return auditService.getAudits(pageSegment,pageSize,latest);
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
	@RequestMapping(value = "latest/{instanceId}",method = RequestMethod.GET)
	public List<AuditEntity> getLatestAuditListForInstance(
			@PathVariable(value = "instanceId") String instanceId) {
		List<AuditEntity> auditEntities = auditService
				.getLatestDistinctAuditsList(instanceId);
		return auditEntities;
	}

	@ApiOperation(value = "Count of audit list based on logged in user",nickname = "Count of audit list based on logged in user")
	@RequestMapping(value = "count",method = RequestMethod.GET)
	public Long countAuditEntityByUserName(){
		String userName = AuditContextHolder.getContext().getInitiator();
		return auditService.countByUserName(userName);
	}
}
