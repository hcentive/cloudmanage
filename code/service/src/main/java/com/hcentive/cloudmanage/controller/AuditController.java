package com.hcentive.cloudmanage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

}
