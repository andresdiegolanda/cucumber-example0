package com.adlanda;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class HttpIntegrationGherkinTest {

	@Autowired
	HttpIntegrationTest httpIntegrationTest;

	@When("I send a {string} to URL {string} with body {string}")
	public void i_send_a_to_url_with_body(String method, String url, String body) throws IOException {
		httpIntegrationTest.sendRequest(method, url, body);
	}

	@Then("the client receives HTTP status code {string}")
	public void the_client_receives_HTTP_status_code(String code) throws IOException {
		final HttpStatus currentStatusCode = httpIntegrationTest.latestResponse.getTheResponse().getStatusCode();
		assertEquals(code, Integer.toString(currentStatusCode.value()));
	}

	@Then("the HTTP response contains message {string}")
	public void the_response_contains_message(String expectedMessage) {
		String body = httpIntegrationTest.latestResponse.getBody();
		if (!body.contains(expectedMessage)) {
			throw new RuntimeException("body:" + body + " -- does not contain" + expectedMessage);
		}
	}

}
