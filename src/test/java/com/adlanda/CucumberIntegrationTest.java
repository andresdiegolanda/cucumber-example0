package com.adlanda;

import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(features = "src/test/resources/owner.feature", plugin = { "pretty",
		"json:target/cucumber-reports/Cucumber.json" })
public class CucumberIntegrationTest {
}