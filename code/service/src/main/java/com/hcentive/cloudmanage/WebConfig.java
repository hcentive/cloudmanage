package com.hcentive.cloudmanage;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

//Marks this class as configuration SEPARATE
@Configuration
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
}
