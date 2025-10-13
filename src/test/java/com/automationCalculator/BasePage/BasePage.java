package com.automationCalculator.BasePage;

import com.automationCalculator.Driver.Setup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    protected final WebDriver driver;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30);

    public BasePage() {
        this.driver = Setup.driver;
    }

    public void waitVisibility(WebElement element) {
        new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.visibilityOf(element));
    }

    public void waitClickable(WebElement element) {
        new WebDriverWait(driver, DEFAULT_TIMEOUT)
                .until(ExpectedConditions.elementToBeClickable(element));
    }

    public void goToUrl(String url) {
        driver.get(url);
    }
}
