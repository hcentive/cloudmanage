package com.hcentive.cloudmanage.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

/**
 * LDAP Authorities mapper, Maps LDAP groups to application specific authority.
 */
public class LDAPGrantedAuthorityMapper implements GrantedAuthoritiesMapper {

	private static final Logger logger = LoggerFactory
			.getLogger(LDAPGrantedAuthorityMapper.class.getName());

	@Autowired
	private RoleMapperRepository roleMapperRepository;

	@Override
	public Collection<? extends GrantedAuthority> mapAuthorities(
			Collection<? extends GrantedAuthority> authorities) {
		List<AppAuthority> appAuthorities = new ArrayList<>();
		// Loop and set.
		for (GrantedAuthority authority : authorities) {
			String ldapName = authority.getAuthority();
			List<LDAPAuthority> ldapRoleRef = new ArrayList<>();
			LDAPAuthority findResult = roleMapperRepository
					.findByLdapAuthName(ldapName);
			if (findResult != null) {
				ldapRoleRef.add(findResult);
				for (LDAPAuthority ldapRole : ldapRoleRef) {
					appAuthorities.addAll(ldapRole.getAppAuthority());
				}
			}
		}
		if(appAuthorities.size() == 0){
			throw new RuntimeException("User is not setup with AD group - please contact IT");
		}
		int nextId = appAuthorities.get(appAuthorities.size() - 1)
				.getAppAuthId() + 1;
		// Retain the original authority as well.
		for (GrantedAuthority authority : authorities) {
			appAuthorities.add(new AppAuthority(nextId++, authority
					.getAuthority()));
		}
		logger.debug("Merged authorities for the current Principal are "
				+ appAuthorities);

		return new HashSet<AppAuthority>(appAuthorities);
	}
}
