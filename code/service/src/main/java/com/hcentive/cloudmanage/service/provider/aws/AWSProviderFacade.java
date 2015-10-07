package com.hcentive.cloudmanage.service.provider.aws;

import java.nio.file.AccessDeniedException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.domain.Instance;
import com.hcentive.cloudmanage.domain.VPC;
import com.hcentive.cloudmanage.service.provider.InstanceService;
import com.hcentive.cloudmanage.service.provider.ProviderFacade;
import com.hcentive.cloudmanage.service.provider.VPCService;

@Service
public class AWSProviderFacade implements ProviderFacade{
	
	
	@Autowired
	private VPCService vpcService;
	
	@Autowired
	private InstanceService instanceService;
	
	@Override
	public List<VPC> getVPCList() {
		return vpcService.list();
	}

	@Override
	public List<Instance> getInstanceList(List<String> groups) throws AccessDeniedException {
		return instanceService.list(groups);
	}

	@Override
	public List<Instance> getAllInstanceList() {
		return instanceService.list();
	}

	@Override
	public void stopInstances(String... instanceIDs) {
		instanceService.stopInstances(instanceIDs);
		
	}
	
	@Override
	public void startInstances(String... instanceIDs){
		instanceService.startInstances(instanceIDs);
	}

	@Override
	public void terminateInstances(String... instanceIDs) {
		instanceService.terminateInstances(instanceIDs);
		
	}
	
	
	

}
