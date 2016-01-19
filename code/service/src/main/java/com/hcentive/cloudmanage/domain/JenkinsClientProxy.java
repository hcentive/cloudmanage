package com.hcentive.cloudmanage.domain;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.http.client.methods.HttpGet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hcentive.cloudmanage.AppConfig;
import com.hcentive.cloudmanage.jenkins.BasicAuthRestTemplate;

@Service
public class JenkinsClientProxy {

	private static final Logger logger = LoggerFactory
			.getLogger(JenkinsClientProxy.class.getName());
	
	public RestTemplate getRestTemplate(){
		return new BasicAuthRestTemplate(AppConfig.jenkinsUsername, AppConfig.jenkinsPassword);
	}

}
