package com.hcentive.cloudmanage.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

@Configuration
@EnableWebMvcSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Value("${ad.url}")
	private String url;
	
	@Value("${ad.domain}")
	private String domain;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests()
				.antMatchers("/static/**").permitAll()
				.anyRequest().fullyAuthenticated();
	}
	
	@Override
    protected void configure(AuthenticationManagerBuilder authManagerBuilder) throws Exception {
         authManagerBuilder.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
    }
	
	@Bean
    public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
         ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(domain, url);
         provider.setConvertSubErrorCodesToExceptions(true);
         provider.setUseAuthenticationRequestCredentials(true);
         return provider;
    }
}