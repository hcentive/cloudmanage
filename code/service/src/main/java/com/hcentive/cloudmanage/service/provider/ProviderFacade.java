package com.hcentive.cloudmanage.service.provider;

import java.nio.file.AccessDeniedException;
import java.util.List;

import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.VPC;

public interface ProviderFacade {
	
	List<VPC> getVPCList();
	
	List<Instance> getInstanceList(List<String> groups) throws AccessDeniedException;
	
	List<Instance> getAllInstanceList();

}
