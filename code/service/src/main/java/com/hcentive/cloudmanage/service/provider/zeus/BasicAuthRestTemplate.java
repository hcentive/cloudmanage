package com.hcentive.cloudmanage.service.provider.zeus;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

public class BasicAuthRestTemplate extends RestTemplate {
	static {
		HttpsURLConnection
				.setDefaultHostnameVerifier((hostname, session) -> true);
	}

	public BasicAuthRestTemplate(String username, String password) {
		addAuthentication(username, password);
	}

	private void addAuthentication(String username, String password) {
		if (username == null) {
			return;
		}
		List<ClientHttpRequestInterceptor> interceptors = Collections
				.<ClientHttpRequestInterceptor> singletonList(new BasicAuthorizationInterceptor(
						username, password));
		setRequestFactory(new InterceptingClientHttpRequestFactory(
				getRequestFactory(), interceptors));
	}

	private static class BasicAuthorizationInterceptor implements
			ClientHttpRequestInterceptor {

		private final String username;
		private final String password;

		final static Logger logger = LoggerFactory
				.getLogger(BasicAuthorizationInterceptor.class);

		public BasicAuthorizationInterceptor(String username, String password) {
			this.username = username;
			this.password = (password == null ? "" : password);
		}

		@Override
		public ClientHttpResponse intercept(HttpRequest request, byte[] body,
				ClientHttpRequestExecution execution) throws IOException {
			byte[] token = Base64.getEncoder().encode(
					(this.username + ":" + this.password).getBytes());
			request.getHeaders().add("Authorization",
					"Basic " + new String(token));
			traceRequest(request, body);
			ClientHttpResponse response = execution.execute(request, body);
			traceResponse(response);
			return response;
		}

		private void traceRequest(HttpRequest request, byte[] body)
				throws IOException {
			logger.info("===========================request begin================================================");
			logger.info("URI         : {}", request.getURI());
			logger.info("Method      : {}", request.getMethod());
			logger.info("Headers     : {}", request.getHeaders());
			logger.info("Request body: {}", new String(body, "UTF-8"));
			logger.info("==========================request end================================================");
		}

		private void traceResponse(ClientHttpResponse response)
				throws IOException {
			StringBuilder inputStringBuilder = new StringBuilder();
			BufferedReader bufferedReader = new BufferedReader(
					new InputStreamReader(response.getBody(), "UTF-8"));
			String line = bufferedReader.readLine();
			while (line != null) {
				inputStringBuilder.append(line);
				inputStringBuilder.append('\n');
				line = bufferedReader.readLine();
			}
			logger.info("============================response begin==========================================");
			logger.info("Status code  : {}", response.getStatusCode());
			logger.info("Status text  : {}", response.getStatusText());
			logger.info("Headers      : {}", response.getHeaders());
			logger.info("Response body: {}", inputStringBuilder.toString());
			logger.info("=======================response end=================================================");
		}

	}

}
