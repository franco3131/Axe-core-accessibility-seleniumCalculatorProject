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

  @Before(order = 0)
  public void startBrowser() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions opts = new ChromeOptions();
    opts.addArguments("--headless=new","--no-sandbox","--disable-dev-shm-usage");
    if ("true".equalsIgnoreCase(System.getenv("CI"))) {
      opts.addArguments("--window-size=1920,1080");
    }
    driver = new ChromeDriver(opts);
  }

  // run as late as possible so it fires after other @After hooks
  @After(order = 1000)
  public void runAxeAndQuit(Scenario scenario) {
    if (driver == null) return;
    // try {
    //   // (optional) patch trivial issues
    //   try {
    //     System.out.println("hello1");
    //     ((JavascriptExecutor) driver).executeScript(
    //       "if(!document.title) document.title='Calculator';" +
    //       "if(!document.documentElement.getAttribute('lang')) document.documentElement.setAttribute('lang','en');"
    //     );
    //   } catch (Exception ignored) {}
    //     System.out.println("hello2");
    //   // IMPORTANT: axe first, then quit
    //   AxeChecks.assertNoWcagAA(driver, scenario.getName());
    //           System.out.println("hello3");
    // } finally {
      try { driver.quit(); } catch (Exception ignored) {}
      driver = null;
  //   }
  // }
  }
}
