package com.hcentive.cloudmanage.service.provider.aws;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.security.DecisionMapper;
import com.hcentive.cloudmanage.security.DecisionMapperRepository;

@Service
public class DecisionMapperServiceImpl implements DecisionMapperService{
	
	private static final Logger logger = LoggerFactory
			.getLogger(DecisionMapperServiceImpl.class.getName());
	@Autowired
	private DecisionMapperRepository decisionMapperRepository;

	@Override
	public Set<DecisionMapper> getDecisionMap() {
		Set<DecisionMapper> decisionMapper = new HashSet<>();
		// Dummy for the time being.
		Collection<? extends GrantedAuthority> authorities = SecurityContextHolder
				.getContext().getAuthentication().getAuthorities();
		for (GrantedAuthority auth : authorities) {
			decisionMapper.addAll(decisionMapperRepository.findByRole(auth
					.getAuthority()));
		}
		logger.info("Mappers available " + decisionMapper);
		return decisionMapper;
	}

}
