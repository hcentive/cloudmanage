package com.hcentive.cloudmanage.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

	@Autowired
	private AuditRepository auditRepository;

	private final Integer PAGE_SEGMENT_INDEX = 0;
	private final Integer PAGE_SIZE = 50;
	private final Boolean SORT_ORDER = true;

	@Override
	@Transactional
	public void audit(AuditEntity auditEntity) {
		auditRepository.save(auditEntity);
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

	@Override
	public List<AuditEntity> getAudits(Integer pageSegment,Integer pageSize,Boolean latest) {
		List<AuditEntity> auditEntities = new ArrayList<>();
		Page<AuditEntity> auditEntityPage;
		String loggedInUser = AuditContextHolder.getContext().getInitiator();

		// Validations
		if(pageSegment == null || pageSegment < 0) pageSegment = PAGE_SEGMENT_INDEX;
		if(pageSize == null || pageSize == 0) pageSize = PAGE_SIZE;
		if(latest == null) latest = SORT_ORDER;

		if(latest){
			// sort by event time desc (latest added record first)
			pageSegment = Math.toIntExact(Math.floorDiv(auditRepository.countByUserName(loggedInUser), pageSize) - pageSegment);
		}
		auditEntityPage = auditRepository.findByUserName(loggedInUser,new PageRequest(pageSegment,pageSize));
		auditEntityPage.getContent().forEach(auditEntity -> auditEntities.add(auditEntity));
		if(latest) Collections.reverse(auditEntities);
		return auditEntities;
	}

	@Override
	public Long countByUserName(String userName) {
		if(userName == null || userName.isEmpty()){
			throw new IllegalArgumentException("User name cannot be null or empty");
		}
		return auditRepository.countByUserName(userName);
	}
}
