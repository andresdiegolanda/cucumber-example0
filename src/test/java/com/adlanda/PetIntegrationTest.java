package com.adlanda;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.messages.internal.com.google.common.collect.Iterables;

@SpringBootTest
public class PetIntegrationTest {

//	static final HttpIntegrationTest httpIntegrationTest = new HttpIntegrationTest();
//	static final JdbcIntegrationTest jdbcIntegrationTest = new JdbcIntegrationTest();

	@Autowired
	JdbcIntegrationTest jdbcIntegrationTest;
	@Autowired
	HttpIntegrationTest httpIntegrationTest;

	Long recordsRetrievedFromDB;

	String id;

	@Given("I know how many owners are in DB")
	public void i_know_how_many_owners_are_in_db() {
		jdbcIntegrationTest.sendQuery("SELECT count(*) FROM owners;");
		recordsRetrievedFromDB = (Long) Iterables.get(jdbcIntegrationTest.latestResult.get(0).values(), 0);
	}

	@When("I retrieve all owners using REST API")
	public void i_retrieve_all_owners_using_rest_api() throws IOException {
		httpIntegrationTest.sendRequest("GET", "http://localhost:9966/petclinic/api/owners", "empty.txt");
	}

	@Then("the number of retrieved records is the same")
	public void the_number_of_retrieved_records_is_the_same() throws JsonMappingException, JsonProcessingException {
		String body = httpIntegrationTest.latestResponse.getBody();
		ArrayList<LinkedHashMap> pets = new ObjectMapper().readValue(body, ArrayList.class);
		if (recordsRetrievedFromDB != pets.size()) {
			throw new RuntimeException(
					"Should have recovered " + recordsRetrievedFromDB + " records, but recovered " + pets.size());
		}
	}

	@Given("Test record is not in DB")
	public void test_record_is_not_in_db() {
		jdbcIntegrationTest.sendQuery("SELECT count(*) FROM owners where first_name='TestOwner';");
		recordsRetrievedFromDB = (Long) Iterables.get(jdbcIntegrationTest.latestResult.get(0).values(), 0);
		assertEquals(new Long(0L), recordsRetrievedFromDB);
	}

	@When("I insert test record using REST API")
	public void i_insert_test_record_using_rest_api() throws IOException, InterruptedException {
		httpIntegrationTest.sendRequest("POST", "http://localhost:9966/petclinic/api/owners", "TestOwner.json");
		Thread.sleep(1000);
	}

	@Given("Test record is in DB")
	public void test_record_is_in_db() {
		jdbcIntegrationTest.sendQuery("SELECT count(*) FROM owners where first_name='TestOwner';");
		recordsRetrievedFromDB = (Long) Iterables.get(jdbcIntegrationTest.latestResult.get(0).values(), 0);
		assertEquals(new Long(1L), recordsRetrievedFromDB);
	}

	@When("I delete test record")
	public void i_delete_test_record() throws IOException {
		jdbcIntegrationTest.sendQuery("SELECT id FROM owners where first_name='TestOwner';");
		String id = Iterables.get(jdbcIntegrationTest.latestResult.get(0).values(), 0).toString();
		httpIntegrationTest.sendRequest("DELETE", "http://localhost:9966/petclinic/api/owners/" + id, "empty.txt");
	}
}
