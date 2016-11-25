package com.hcentive.cloudmanage;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hcentive.cloudmanage.audit.AuditContext;
import com.hcentive.cloudmanage.audit.AuditContextHolder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

//Marks this class as configuration SEPARATE
@Configuration
@PropertySource("classpath:application.properties")
@PropertySource(value = "classpath:application-${env}.properties", ignoreResourceNotFound = true)
@ComponentScan(basePackages = { "com.hcentive.*.controller" })
@EnableWebMvc
public class WebConfig extends WebMvcConfigurerAdapter {

	// As we have extended - lets make sure to override
	// configureDefaultServletHandling with configurer.enable();
	@Override
	public void configureDefaultServletHandling(
			DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	// Add Jackson JSON conversion configuration to handle quartz classes to be
	// converted skipping setters or attributes only
	@Override
	public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
		for (HttpMessageConverter<?> converter : converters) {
			if (converter instanceof MappingJackson2HttpMessageConverter) {
				MappingJackson2HttpMessageConverter jsonMessageConverter = (MappingJackson2HttpMessageConverter) converter;
				ObjectMapper objectMapper = jsonMessageConverter
						.getObjectMapper();
				// OR disable SerializationFeature.FAIL_ON_EMPTY_BEANS
				objectMapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
				break;
			}
		}
	}

	@Override
	  public void addInterceptors(InterceptorRegistry registry) {
	   
	    registry.addInterceptor(new HandlerInterceptor() {

			@Override
			public void afterCompletion(HttpServletRequest arg0,
					HttpServletResponse arg1, Object arg2, Exception arg3)
					throws Exception {
				
				
			}

			@Override
			public void postHandle(HttpServletRequest arg0,
					HttpServletResponse arg1, Object arg2, ModelAndView arg3)
					throws Exception {
				
			}

			@Override
			public boolean preHandle(HttpServletRequest arg0,
					HttpServletResponse arg1, Object arg2) throws Exception {
				Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
				if(authentication != null){
					AuditContext auditContext = new AuditContext();
					auditContext.setInitiator(authentication.getName());
					AuditContextHolder.setContext(auditContext);
				}
				return true;
			}
	    });
	  }
}
