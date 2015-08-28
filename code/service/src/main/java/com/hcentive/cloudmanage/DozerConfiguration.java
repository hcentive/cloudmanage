package com.hcentive.cloudmanage;

import org.dozer.DozerBeanMapper;
import org.dozer.loader.api.BeanMappingBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.services.ec2.model.Vpc;
import com.hcentive.cloudmanage.domain.VPC;

@Configuration
public class DozerConfiguration {
	


	@Bean
	public DozerBeanMapper dozerMapper(){
		DozerBeanMapper mapper = new DozerBeanMapper();
		mapper.addMapping(awsMapping());
		return mapper;
	}
	
	private BeanMappingBuilder awsMapping(){
		BeanMappingBuilder builder = new BeanMappingBuilder(){
			protected void configure() {
		        mapping(VPC.class, Vpc.class);
//		                .fields("tags", "tags",
//		                    customConverter("org.dozer.CustomConverter")
//		                );
		      }
		    };
		
		return builder;
	}
	    
	

}
