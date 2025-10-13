package com.automationCalculator.Steps;

import com.automationCalculator.accessibility.AxeChecks;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;

import static com.automationcalculator.support.Hooks.driver; // wherever you keep WebDriver

public class AccessibilitySteps {
    @Then("the page has no WCAG AA accessibility violations")
    public void thePageHasNoWcagAAViolations() {
        WebDriver d = driver; // get your active driver
        AxeChecks.assertNoWcagAA(d, d.getTitle().replaceAll("\\W+","_"));
    }
}
