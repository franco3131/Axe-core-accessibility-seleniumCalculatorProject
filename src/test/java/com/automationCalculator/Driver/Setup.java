package com.automationCalculator.Driver;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;     // 
import com.automationCalculator.accessibility.AxeChecks;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;

public class Setup {
  public static WebDriver driver;

  @Before
  public void startBrowser() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions opts = new ChromeOptions();
    opts.addArguments("--headless=new","--no-sandbox","--disable-dev-shm-usage");
    driver = new ChromeDriver(opts);
  }

  @After
  public void runAxeAndQuit(Scenario scenario) {
    if (driver == null) return;
      try { driver.quit(); } catch (Exception ignored) {}
      driver = null;

  }
}
