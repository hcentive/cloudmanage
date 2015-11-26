package com.hcentive.cloudmanage.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;

/**
 * LDAP Authorities mapper, Maps LDAP groups to application specific authority.
 */
public class LDAPGrantedAuthorityMapper implements GrantedAuthoritiesMapper {

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

		return new HashSet<AppAuthority>(appAuthorities);
	}
}
