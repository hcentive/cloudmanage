package com.hcentive.cloudmanage.audit;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditServiceImpl implements AuditService {

	@Autowired
	private AuditRepository auditRepository;

	@Override
	@Transactional
	public void audit(AuditEntity auditEntity) {
		auditRepository.save(auditEntity);
	}

	@Override
	public List<AuditEntity> getAuditsList() {
		return (List<AuditEntity>) auditRepository.findAll();
	}

	@Override
	public List<AuditEntity> getAuditsList(String instanceId) {
		return (List<AuditEntity>) auditRepository.findBySearchTerm(instanceId);
	}

	@Override
	public List<AuditEntity> getLatestDistinctAuditsList(String instanceId) {
		return (List<AuditEntity>) auditRepository
				.findLatestDistinctBySearchTerm(instanceId);
	}
}
