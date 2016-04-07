package com.hcentive.cloudmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;

import com.hcentive.cloudmanage.security.AjaxLogoutSuccessHandler;
import com.hcentive.cloudmanage.security.CustomAccessDeniedHandler;
import com.hcentive.cloudmanage.security.CustomAuthenticationEntryPoint;
import com.hcentive.cloudmanage.security.CustomAuthenticationFailureHandler;
import com.hcentive.cloudmanage.security.CustomAuthenticationSuccessHandler;
import com.hcentive.cloudmanage.security.LDAPGrantedAuthorityMapper;

@Configuration
@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:application-${env}.properties", ignoreResourceNotFound = true)
// WARN: As of Spring Security 4.0, @EnableWebMvcSecurity is deprecated. The
// replacement is
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${ad.url}")
	private String url;

	@Value("${ad.domain}")
	private String domain;

	@Autowired
	CustomAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	CustomAuthenticationSuccessHandler authenticationSuccessHandler;

	@Autowired
	AjaxLogoutSuccessHandler ajaxLogoutSuccessHandler;

	@Autowired
	CustomAccessDeniedHandler customAccessDeniedHandler;

	@Autowired
	CustomAuthenticationFailureHandler customAuthenticationFailureHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/login","/error").permitAll()
				.anyRequest()
				.fullyAuthenticated().and().formLogin()
				.failureHandler(customAuthenticationFailureHandler).and()
				.logout().logoutSuccessHandler(ajaxLogoutSuccessHandler);
		http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(customAccessDeniedHandler);
		http.csrf().disable();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder)
			throws Exception {
		authManagerBuilder
				.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
	}

	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
				domain, url);
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUseAuthenticationRequestCredentials(true);
		provider.setAuthoritiesMapper(authoritiesMapper());
		return provider;
	}

	@Bean
	public GrantedAuthoritiesMapper authoritiesMapper() {
		return new LDAPGrantedAuthorityMapper();
	}
}