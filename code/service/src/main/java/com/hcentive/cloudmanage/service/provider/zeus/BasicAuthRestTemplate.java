package com.hcentive.cloudmanage.service.provider.zeus;

import java.io.IOException;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;

import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.InterceptingClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.hcentive.cloudmanage.AppConfig;

public class BasicAuthRestTemplate extends RestTemplate {
	static {
		Pattern pattern = Pattern.compile("http[s]:\\/\\/(.+?)[:|\\/]");
		Matcher matcher = pattern.matcher(AppConfig.zeusUrl);
		if (matcher.find()) {
			HttpsURLConnection
					.setDefaultHostnameVerifier((hostname, session) -> hostname
							.equals(matcher.group(1)));
		}
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
			return execution.execute(request, body);
		}

	}

}
