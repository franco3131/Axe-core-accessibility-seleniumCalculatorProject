package com.automationCalculator.Driver;

import java.net.URL;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Setup {
 public static WebDriver driver;

    @Before
    public void setup() {
        // Let WebDriverManager fetch the right driver (no hard-coded paths)
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        // Always safe in CI; fine locally too
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");

        // Optional: tweak for CI only
        if ("true".equalsIgnoreCase(System.getenv("CI"))) {
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
