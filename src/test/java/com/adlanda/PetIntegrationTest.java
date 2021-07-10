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

	Long registrosRecuperadosBd;

	String id;

	@Given("Se cuantas mascotas hay en la base de datos")
	public void se_cuantas_mascotas_hay_en_la_base_de_datos() {
		jdbcIntegrationTest.sendQuery("SELECT count(*) FROM pets;");
		registrosRecuperadosBd = (Long) Iterables.get(jdbcIntegrationTest.latestResult.get(0).values(), 0);
	}

	@When("Recupero todas las mascotas usando la API REST")
	public void recupero_todas_las_mascotas_usando_la_api_rest() throws IOException {
		httpIntegrationTest.sendRequest("GET", "http://172.20.176.1:9966/petclinic/api/pets", "empty.txt");
	}

	@Then("el numero de registros recuperados es el mismo")
	public void el_numero_de_registros_recuperados_es_el_mismo() throws JsonMappingException, JsonProcessingException {
		String body = httpIntegrationTest.latestResponse.getBody();
		ArrayList<LinkedHashMap> pets = new ObjectMapper().readValue(body, ArrayList.class);
		if (registrosRecuperadosBd != pets.size()) {
			throw new RuntimeException(
					"Should have recovered " + registrosRecuperadosBd + " records, but recovered " + pets.size());
		}
	}

	@Given("El registro de prueba no esta en la base de datos")
	public void el_registro_de_prueba_no_esta_en_la_base_de_datos() {
		jdbcIntegrationTest.sendQuery("SELECT count(*) FROM pets where name='TestPet';");
		registrosRecuperadosBd = (Long) Iterables.get(jdbcIntegrationTest.latestResult.get(0).values(), 0);
		assertEquals(new Long(0L), registrosRecuperadosBd);
	}

	@When("Inserto el registro de prueba")
	public void inserto_el_registro_de_prueba() throws IOException {
		httpIntegrationTest.sendRequest("POST", "http://172.20.176.1:9966/petclinic/api/pets", "TestPet.json");
	}

	@Given("El registro de prueba esta en la base de datos")
	public void el_registro_de_prueba_esta_en_la_base_de_datos() {
		jdbcIntegrationTest.sendQuery("SELECT count(*) FROM pets where name='TestPet';");
		registrosRecuperadosBd = (Long) Iterables.get(jdbcIntegrationTest.latestResult.get(0).values(), 0);
		assertEquals(new Long(1L), registrosRecuperadosBd);
	}

	@When("Elimino el registro de prueba")
	public void elimino_el_registro_de_prueba() throws IOException {
		jdbcIntegrationTest.sendQuery("SELECT id FROM pets where name='TestPet';");
		String id = Iterables.get(jdbcIntegrationTest.latestResult.get(0).values(), 0).toString();
		httpIntegrationTest.sendRequest("DELETE", "http://172.20.176.1:9966/petclinic/api/pets/" + id, "empty.txt");
	}
}
