package com.adlanda;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;

@SpringBootTest
@Component
public class HttpIntegrationTest {

	private static Logger LOGGER = LoggerFactory.getLogger(HttpIntegrationTest.class);

	ResponseResults latestResponse;

	@Autowired
	protected RestTemplate restTemplate;

	Map<String, String> orchestrationHeaders;

	private Map<String, String> prepareHeaders() throws JsonProcessingException {
		final Map<String, String> headers = new HashMap<>();
		headers.put("Content-Type", "application/json");
		return headers;
	}

	private class ResponseResultErrorHandler implements ResponseErrorHandler {
		private ResponseResults results = null;
		private Boolean hadError = false;

		private ResponseResults getResults() {
			return results;
		}

		@Override
		public boolean hasError(ClientHttpResponse response) throws IOException {
			hadError = response.getRawStatusCode() >= 400;
			return hadError;
		}

		@Override
		public void handleError(ClientHttpResponse response) throws IOException {
			results = new ResponseResults(response);
		}
	}

	void sendRequest(String method, String url, String bodyFile) throws IOException {
		final Map<String, String> headers = prepareHeaders();
		final HeaderSettingRequestCallback requestCallback = new HeaderSettingRequestCallback(headers);
		final ResponseResultErrorHandler errorHandler = new ResponseResultErrorHandler();

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		File request;
		if (classLoader.getResource(bodyFile) != null) {
			URL fileUrl = classLoader.getResource(bodyFile);
			request = new File(fileUrl.getPath());
		} else {
			request = new File(bodyFile);
		}
		String requestBody = FileUtils.readFileToString(request, "UTF-8");
		restTemplate.setErrorHandler(errorHandler);
		requestCallback.setBody(requestBody);
		latestResponse = restTemplate.execute(url, HttpMethod.valueOf(method), requestCallback, response -> {
			if (errorHandler.hadError) {
				return (errorHandler.getResults());
			} else {
				return (new ResponseResults(response));
			}
		});
		LOGGER.info("latestResponse:{}", latestResponse);
	}

}
