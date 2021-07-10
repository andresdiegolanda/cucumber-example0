package com.adlanda;

import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class WebIntegrationGherkinTest {

	WebDriver driver;

	@When("llamo a la aplicacion petclinic web")
	public void llamo_a_la_aplicacion_petclinic_web() {
		System.setProperty("webdriver.chrome.driver",
				"C:\\_dev\\workspace1\\selenium101\\src\\main\\resources\\chromedriver.exe");
		driver = new ChromeDriver();
		String baseURL = "http://localhost:8080";
		driver.get(baseURL);

	}

	@Then("aparece la pantalla con el menu")
	public void aparece_la_pantalla_con_el_menu() throws InterruptedException {
		WebElement element = driver.findElement(By.id("main-navbar"));
		assertTrue(element.getText().contains("VETERINARIANS"));
	}

	@When("hago clic en Veterinarians")
	public void hago_clic_en_veterinarians() {
		driver.findElement(By.xpath("//*[@id=\"main-navbar\"]/ul/li[3]/a")).click();
	}

	@Then("aparece la lista de veterinarios")
	public void aparece_la_lista_de_veterinarios() throws InterruptedException {
		WebElement element = driver.findElement(By.id("vets"));
		System.out.println(element.getText());
		// assertTrue(element.getText().contains("VETERINARIANS"));
		driver.quit();
	}

}
