package com.hcentive.cloudmanage.service.group;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.hcentive.cloudmanage.domain.Group;

@Service
public class GroupServiceImpl implements GroupService{
	
	private Map<String, String> authGroupMapping;
	
	public GroupServiceImpl(){
		authGroupMapping = new HashMap<String, String>();
		authGroupMapping.put("techops-int", "techops");
		authGroupMapping.put("phix-team", "phix");
		authGroupMapping.put("hix-team", "hix");
	}

	@Override
	public List<String> getGroups() {
		SecurityContext securityContext = SecurityContextHolder.getContext();
		Collection<? extends GrantedAuthority> authorities = securityContext.getAuthentication().getAuthorities();
		List<String> groups = new ArrayList<String>();
		for(GrantedAuthority auth : authorities){
			String authStr = auth.getAuthority();
			String group = authGroupMapping.get(authStr);
			if(group != null){
				groups.add(group);
			}
		}
		return groups;
	}

}
