package com.hcentive.cloudmanage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.ldap.authentication.ad.ActiveDirectoryLdapAuthenticationProvider;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.hcentive.cloudmanage.security.CustomAuthenticationEntryPoint;
import com.hcentive.cloudmanage.security.CustomAuthenticationSuccessHandler;
import com.hcentive.cloudmanage.security.LDAPGrantedAuthorityMapper;

@Configuration
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

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/login", "/logout").permitAll()
				.anyRequest().fullyAuthenticated().and().httpBasic();
		http.logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/").deleteCookies("JSESSIONID")
				.invalidateHttpSession(true).and()
				// Redirect user to in case of no session.
				.sessionManagement().invalidSessionUrl("/login")
				// No more than single session per user.
				.maximumSessions(1);
		http.exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).and()
				.csrf().disable();

	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// This is here to ensure that the static content (JavaScript, CSS, etc)
		// is accessible from the login page without authentication
		web.ignoring().antMatchers("/resources/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder authManagerBuilder)
			throws Exception {
		authManagerBuilder
				.authenticationProvider(activeDirectoryLdapAuthenticationProvider());
	}

	@Bean
	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		ActiveDirectoryLdapAuthenticationProvider provider = new ActiveDirectoryLdapAuthenticationProvider(
				domain, url);
		provider.setConvertSubErrorCodesToExceptions(true);
		provider.setUseAuthenticationRequestCredentials(true);
		return provider;
	}
	
	@Bean
	public GrantedAuthoritiesMapper authoritiesMapper() {
		return new LDAPGrantedAuthorityMapper();
	}
}