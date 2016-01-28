package com.hcentive.cloudmanage.service.provider.aws;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.domain.UserProfile;
import com.hcentive.cloudmanage.security.DecisionMapper;
import com.hcentive.cloudmanage.security.Tag;

@Service
public class UserProfileServiceImpl  implements UserProfileService{
	
	@Autowired
	private DecisionMapperService decisionMapperService;

	@Override
	public UserProfile getUserProfile() {
		Set<DecisionMapper> decisionsMap = decisionMapperService.getDecisionMap();
		UserProfile userProfile = new UserProfile();
		List<Tag> tags = mapTags(decisionsMap);
		userProfile.setTags(tags);
		return userProfile;
	}

	private List<Tag> mapTags(Set<DecisionMapper> decisionsMap) {
		List<Tag> tags = new ArrayList<Tag>();
		for(DecisionMapper decision : decisionsMap){
			Tag tag = decision.getTag();
			tags.add(tag);
		}
		return tags;
	}

}
