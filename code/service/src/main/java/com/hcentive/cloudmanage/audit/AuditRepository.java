package com.hcentive.cloudmanage.audit;

import org.springframework.data.repository.CrudRepository;

public interface AuditRepository extends CrudRepository<AuditEntity, Long> {
}
