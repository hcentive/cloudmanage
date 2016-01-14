package com.hcentive.cloudmanage.domain;

import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hcentive.cloudmanage.AppConfig;

public class JenkinsClientProxy {

	private static final Logger logger = LoggerFactory
			.getLogger(JenkinsClientProxy.class.getName());

	public HttpClient getJenkinsClient() throws IOException {
		HttpClient client = new HttpClient();
		// The Login page
		GetMethod loginLink = new GetMethod(AppConfig.jenkinsUrl + "login");
		client.executeMethod(loginLink);
		checkResult(loginLink.getStatusCode());
		// Login Form
		String location = AppConfig.jenkinsUrl + "j_acegi_security_check";
		while (true) {
			PostMethod loginMethod = new PostMethod(location);
			loginMethod.addParameter("j_username", AppConfig.jenkinsUsername);
			loginMethod.addParameter("j_password", AppConfig.jenkinsPassword);
			loginMethod.addParameter("action", "login");
			client.executeMethod(loginMethod);
			if (loginMethod.getStatusCode() / 100 == 3) {
				// Commons HTTP client refuses to handle redirects for POST
				// so we have to do it manually.
				location = loginMethod.getResponseHeader("Location").getValue();
				continue;
			}
			logger.debug("Status Code " + loginMethod.getStatusCode());
			checkResult(loginMethod.getStatusCode());
			break;
		}
		return client;
	}

	/*
	 * Checks if the http code is 200 or not!
	 */
	private static void checkResult(int i) throws IOException {
		if (i / 100 != 2)
			throw new IOException();
	}
}
