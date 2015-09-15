package com.hcentive.cloudmanage.service.provider;

import java.nio.file.AccessDeniedException;
import java.util.List;

import com.hcentive.cloudmanage.domain.Instance;

public interface InstanceService {
	
	List<Instance> list();

	List<Instance> list(List<String> group) throws AccessDeniedException;

}
