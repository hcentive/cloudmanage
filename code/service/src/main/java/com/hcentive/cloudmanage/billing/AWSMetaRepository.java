package com.hcentive.cloudmanage.billing;

import org.springframework.data.repository.CrudRepository;

public interface AWSMetaRepository extends CrudRepository<AWSMetaInfo, Long> {

	public AWSMetaInfo findByAwsInstanceId(String instanceId);
}
