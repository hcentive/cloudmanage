package com.hcentive.cloudmanage.security;

import org.springframework.data.repository.Repository;


/**
 * Spring data will create an instance at runtime.
 * 
 */
public interface RoleMapperRepository extends
		Repository<LDAPAuthority, String> {

	//@Cacheable(value = "appAuthorityMapCache")
	public LDAPAuthority findByLdapAuthName(String ldapAuthName);
}
