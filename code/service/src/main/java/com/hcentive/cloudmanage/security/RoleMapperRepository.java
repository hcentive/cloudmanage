package com.hcentive.cloudmanage.security;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

/**
 * Spring data will create an instance at runtime.
 * 
 */
@Repository
public interface RoleMapperRepository extends
		org.springframework.data.repository.Repository<LDAPAuthority, String> {

	@Cacheable(value = "appAuthorityMapCache")
	public LDAPAuthority findByLdapAuthName(String ldapAuthName);
}
