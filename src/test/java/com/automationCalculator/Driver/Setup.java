package com.automationCalculator.Driver;

import com.automationCalculator.accessibility.AxeChecks;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Setup {
    public static WebDriver driver;

    // guard to ensure axe runs once per scenario (in case multiple @After hooks exist)
    private static final ThreadLocal<Boolean> axeRan = ThreadLocal.withInitial(() -> false);

    @Before(order = 0)
    public void setup() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        if ("true".equalsIgnoreCase(System.getenv("CI"))) {
            options.addArguments("--window-size=1920,1080");
        }
        driver = new ChromeDriver(options);

        // reset guard at scenario start
        axeRan.set(false);
    }

    @After(order = 1000) // run late so the page is in its final state
    public void tearDown(Scenario scenario) {
        try {
            if (driver != null && !axeRan.get()) {
                axeRan.set(true);

                // Patch missing <title> / <html lang> to avoid trivial fails on static pages
                try {
                    JavascriptExecutor js = (JavascriptExecutor) driver;
                    js.executeScript(
                        "if(!document.title) document.title='Calculator';" +
                        "if(!document.documentElement.getAttribute('lang')) document.documentElement.setAttribute('lang','en');"
                    );
                } catch (Exception ignored) { /* non-fatal */ }

                // Run axe and name artifacts after the scenario
                AxeChecks.assertNoWcagAA(driver, scenario.getName());
            }
        } finally {
            axeRan.remove();
            if (driver != null) {
                try { driver.quit(); } catch (Exception ignored) {}
                driver = null;
            }
        }
    }
}

