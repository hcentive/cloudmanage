package com.hcentive.cloudmanage.jenkins;

import org.springframework.data.repository.CrudRepository;

public interface HostMetaInfoRepository extends
		CrudRepository<HostMetaInfo, Long> {
	
	public HostMetaInfo findByHostName(String hostName);
}
