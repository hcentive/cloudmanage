package com.hcentive.security;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.hcentive.dao.GenericDAO;
import com.hcentive.domain.Role;

public class CloudManageAuthenticationProvider implements AuthenticationProvider,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8374648663929088496L;

	@Autowired
    private AuthenticationProvider delegate;
    
    @SuppressWarnings("rawtypes")
	private GenericDAO genericDAO;

    public void setGenericDAO(@SuppressWarnings("rawtypes") GenericDAO genericDAO) {
		this.genericDAO = genericDAO;
	}

	public CloudManageAuthenticationProvider(AuthenticationProvider delegate) {
        this.delegate = delegate;
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException{
         final Authentication a = delegate.authenticate(authentication);

        // Load additional authorities and create an Authentication object
        
        final List<GrantedAuthority> authorities = loadRolesFromDatabase(a.getName());
        
        UsernamePasswordAuthenticationToken auth=new UsernamePasswordAuthenticationToken(a.getPrincipal(), a.getCredentials(),authorities);
        
        System.out.println(a.getCredentials());
        auth.eraseCredentials();
        return auth;

       /* return new UsernamePasswordAuthenticationToken(authorities) {
            *//**
			 * 
			 *//*
			private static final long serialVersionUID = 4688626959894854200L;

			public Object getCredentials() {
                throw new UnsupportedOperationException();
            }

            public Object getPrincipal() {
                return a.getPrincipal();
            }

			@Override
			public Object getCredentials() {
				// TODO Auto-generated method stub
				return a.getCredentials();
			}
        };*/
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //return delegate.supports(authentication);
    	return true;
    }
    private List<GrantedAuthority> loadRolesFromDatabase(String username) throws AuthenticationException
    {
    	List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
    	List<Role> roles = getRoles(username);
    	for (Role role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
			String[] permissions = getPermissions(role.getRole());
			if (null != permissions) {
				for (String perm : permissions) {
					authorities.add(new SimpleGrantedAuthority(perm));
				}
			}
		}
    	return authorities;
        
    }
    @SuppressWarnings("unchecked")
	private List<Role> getRoles(String username) throws AuthenticationException {
		Map<String, Object> queryParameters = new HashMap<String, Object>();
		queryParameters.put("username", username);		
	/*	@SuppressWarnings("rawtypes")
		List roleIdList= genericDAO.findByNamedQuery("role.getRoleIDForUser", queryParameters);
		String [] paramArr={"roleIdList"};*/
		return genericDAO.findByNamedQuery("role.getRolesByUsername", queryParameters);
	}
    private String[] getPermissions(String role) throws AuthenticationException {
		Map<String, Object> queryParameters = new HashMap<String, Object>();
		queryParameters.put("role", role);

		List<String> results = (List<String>) genericDAO.findByNamedQuery("permission.getPermissionsForRole", queryParameters);
		if (null == results || results.isEmpty()) {
			return null;
		}
		
		return results.toArray(new String[results.size()]);
	} 
}