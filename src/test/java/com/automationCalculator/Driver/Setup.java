package com.automationCalculator.Driver;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;

public class Setup {
	public static WebDriver driver;
	@Before
	public void setup() throws MalformedURLException {
	 if(System.getProperty("os.name").toLowerCase().contains("mac")) {
	  System.setProperty("webdriver.chrome.driver", "/Users/davidfranco/eclipse-workspace/mavenProject2/calculatorProject/WebDrivers/chromedriver 3");
	  driver = new ChromeDriver();
	 }
	 else {
//	  System.setProperty("webdriver.chrome.driver", "/usr/bin/chromedriver");
//	  ChromeOptions options = new ChromeOptions();
//	  options.addArguments("headless");
//	  driver = new ChromeDriver(options);
		 DesiredCapabilities dc=DesiredCapabilities.chrome();
		 ChromeOptions options=new ChromeOptions();
		 options.addArguments("headless");
		 dc.setCapability(ChromeOptions.CAPABILITY, options);
		 driver= new RemoteWebDriver(new URL("http://172.31.19.201:4444/wd/hub"),dc);
	 }
	}
	@After
	public void tearDown() {
		  driver.quit();
	}
}
