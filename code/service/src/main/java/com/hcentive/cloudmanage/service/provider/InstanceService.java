package com.hcentive.cloudmanage.service.provider;

import java.nio.file.AccessDeniedException;
import java.util.List;

import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.ResourceMetaData;

public interface InstanceService {
	
	List<Instance> list();

	List<Instance> list(List<String> group) throws AccessDeniedException;
	
	ResourceMetaData getInstancesMetaData();

	void stopInstances(String... instanceIDs);

	void startInstances(String... instanceIDs);

	void terminateInstances(String... instanceIDs);

}
