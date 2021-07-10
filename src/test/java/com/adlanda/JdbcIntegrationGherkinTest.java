package com.adlanda;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class JdbcIntegrationGherkinTest {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JdbcIntegrationGherkinTest.class);

	@Autowired
	JdbcIntegrationTest jdbcIntegrationTest;

	@When("I send query {string}")
	public void i_send_query(String query) {
		jdbcIntegrationTest.sendQuery(query);
	}

	@Then("the response contains message {string}")
	public void the_response_contains_message(String expectedMessage) {
		String result = jdbcIntegrationTest.latestResult.toString();
		LOGGER.info("String result:{}", result);
		if (!result.contains(expectedMessage)) {
			throw new RuntimeException("result:" + result + "-- does not contain" + expectedMessage);
		}
	}

}
