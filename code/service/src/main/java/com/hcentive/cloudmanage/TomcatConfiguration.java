package com.hcentive.cloudmanage;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.AbstractProtocol;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class TomcatConfiguration {
	
	@Value("${tomcat.ajp.port}")
	private int ajpPort;

	@Value("${tomcat.ajp.enabled}")
	private boolean tomcatAjpEnabled;


	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
	    if (tomcatAjpEnabled)
	    {
	        Connector ajpConnector = new Connector("AJP/1.3");
	        ajpConnector.setProtocol("AJP/1.3");
	        ajpConnector.setPort(ajpPort);
	        ajpConnector.setSecure(false);
	        ajpConnector.setAllowTrace(false);
	        ajpConnector.setScheme("http");
	        tomcat.addAdditionalTomcatConnectors(ajpConnector);
	        tomcat.addConnectorCustomizers(new TomcatConnectorCustomizer() {
	            @Override
	            public void customize(Connector connector) {
	                ((AbstractProtocol) connector.getProtocolHandler()).setConnectionTimeout(2*60*1000);
	            }
	        });

	    }

	    return tomcat;
	}

}