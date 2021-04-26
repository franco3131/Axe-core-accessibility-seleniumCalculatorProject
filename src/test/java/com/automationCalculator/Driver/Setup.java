package com.automationCalculator.Driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Setup {
	public static WebDriver driver;
	@Before
	public void setup() {
	 if(System.getProperty("os.name").toLowerCase().contains("mac")) {
	  System.setProperty("webdriver.chrome.driver", "/Users/davidfranco/eclipse-workspace/mavenProject2/calculatorProject/WebDrivers/chromedriver 3");
	 }
	 else {
		  System.setProperty("webdriver.chrome.driver", "/Users/davidfranco/eclipse-workspace/mavenProject2/calculatorProject/WebDrivers/chromedriver");

	 }
	  driver = new ChromeDriver();
	  DesiredCapabilities cap = DesiredCapabilities.chrome();
      cap.setCapability("applicationCacheEnabled", false);
	  driver.manage().window().maximize();
		  System.out.println("hi");
	}
	@After
	public void tearDown() {
		  driver.quit();
	}
}
