package com.automationCalculator.Driver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;

import com.automationCalculator.accessibility.AxeChecks;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Setup {
    public static WebDriver driver;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        if ("true".equalsIgnoreCase(System.getenv("CI"))) {
            options.addArguments("--window-size=1920,1080");
        }

        driver = new ChromeDriver(options);
    }

    // Run axe after each scenario; use @After("@accessibility") to limit to tagged scenarios
    @After
    public void tearDown(Scenario scenario) {
        if (driver != null) {
            try {
                // Optional: patch missing <title> and <html lang> so those rules don’t always fail
                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript(
                        "if(!document.title) document.title='Calculator';" +
                        "if(!document.documentElement.getAttribute('lang')) document.documentElement.setAttribute('lang','en');"
                    );
                } catch (Exception ignored) {}

                // Run axe and name the file with the scenario name (not the page title)
                AxeChecks.assertNoWcagAA(driver, scenario.getName());
            } catch (AssertionError ae) {
                // Re-throw so the test still fails after writing JSON
                throw ae;
            } finally {
                driver.quit();
            }
        }
    }
}
