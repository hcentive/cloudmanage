package com.hcentive.cloudmanage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hcentive.cloudmanage.audit.AuditEntity;
import com.hcentive.cloudmanage.audit.AuditService;

@RestController
@RequestMapping("/audit")
public class AuditController {

	@Autowired
	private AuditService auditService;

	@RequestMapping(value = "list")
	public List<AuditEntity> getAuditList() {
		List<AuditEntity> auditEntities = auditService.getAuditsList();
		return auditEntities;
	}

	@RequestMapping(value = "list/{instanceId}")
	public List<AuditEntity> getAuditListForInstance(
			@PathVariable(value = "instanceId") String instanceId) {
		List<AuditEntity> auditEntities = auditService
				.getAuditsList(instanceId);
		return auditEntities;
	}

	@RequestMapping(value = "list/latest/{instanceId}")
	public List<AuditEntity> getLatestAuditListForInstance(
			@PathVariable(value = "instanceId") String instanceId) {
		List<AuditEntity> auditEntities = auditService
				.getLatestDistinctAuditsList(instanceId);
		return auditEntities;
	}
}
