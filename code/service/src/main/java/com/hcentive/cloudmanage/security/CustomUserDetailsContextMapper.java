package com.hcentive.cloudmanage.security;

import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.Name;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.AttributesMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.ContainerCriteria;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.ldap.userdetails.UserDetailsContextMapper;

public class CustomUserDetailsContextMapper implements UserDetailsContextMapper {

	private static final Logger logger = LoggerFactory
			.getLogger(CustomUserDetailsContextMapper.class.getName());

	@Autowired
	private LdapTemplate ldapTemplate;

	public void setLdapTemplate(LdapTemplate ldapTemplate) {
		this.ldapTemplate = ldapTemplate;
	}

	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx,
			String username, Collection<? extends GrantedAuthority> authorities) {
		logger.debug("Existing Authorities are {}", authorities);
		Name dn = ctx.getDn();
		if (dn != null && dn.size() > 0) {
			username = dn.get(dn.size() - 1).substring("CN=".length());
		} else {
			throw new RuntimeException("User is not setup with AD - contact IT");
		}
		// Fetch All authorities in a custom way
		List<GrantedAuthority> mappedAuthorities = getMemberOf(username);
		logger.debug("Fetched Authorities are {}", mappedAuthorities);
		// Merge both the lists. // Infact can get rid of the default one.
		Set<GrantedAuthority> set = new HashSet<GrantedAuthority>(authorities);
		set.addAll(mappedAuthorities);
		List<GrantedAuthority> mergeList = new ArrayList<GrantedAuthority>(set);
		// Best Practice: Password need not be saved; a token instead.
		logger.debug("Merged list of Authorities is {}", mergeList);
		return new User(username, "", mergeList);
	}

	@Override
	public void mapUserToContext(UserDetails user, DirContextAdapter ctx) {
		throw new UnsupportedOperationException();
	}

	private class PersonAttributesMapper implements
			AttributesMapper<List<GrantedAuthority>> {
		private List<GrantedAuthority> groups = new ArrayList<GrantedAuthority>();

		@Override
		public List<GrantedAuthority> mapFromAttributes(
				Attributes paramAttributes) throws NamingException {
			Attribute attr = paramAttributes.get("memberOf");
			@SuppressWarnings("unchecked")
			// Sorry to use Enumeration in 2016!
			List<String> groupsList = (List<String>) Collections.list(attr
					.getAll());
			for (String group : groupsList) {
				String[] names = group.split(",");
				if (names != null && names.length > 0) {
					String authName = names[0].substring("CN=".length());
					GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(
							authName);
					groups.add(grantedAuthority);
				}
			}
			return groups;
		}
	}

	/*
	 * Returns a list of search results.
	 */
	private List<GrantedAuthority> getMemberOf(String name) {
		logger.debug("Retrieve MemberOf Attribute values for {}", name);
		ContainerCriteria query = query()
				.base("OU=Emp,OU=ALL-Noida,OU=Encrypted-Mail,OU=Google")
				.where("objectclass").is("Person").and("cn").is(name);
		AttributesMapper<List<GrantedAuthority>> mapper = new PersonAttributesMapper();
		// We assume it to be just one.
		return ldapTemplate.search(query, mapper).get(0);
	}
}
