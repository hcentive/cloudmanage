package com.hcentive.cloudmanage.service.provider.aws;

import java.util.Set;

import com.hcentive.cloudmanage.security.DecisionMapper;

public interface DecisionMapperService {
	Set<DecisionMapper> getDecisionMap();
}
