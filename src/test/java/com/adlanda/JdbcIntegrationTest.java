package com.adlanda;

import java.util.List;
import java.util.Map;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import io.cucumber.spring.CucumberContextConfiguration;

@SpringBootTest
@CucumberContextConfiguration
public class JdbcIntegrationTest {

	private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(JdbcIntegrationTest.class);
	List<Map<String, Object>> latestResult;

	@Autowired
	JdbcTemplate jdbcTemplate;

	void sendQuery(String query) {
		latestResult = jdbcTemplate.queryForList(query);
		LOGGER.info("Query result:{}", latestResult);

	}

}
