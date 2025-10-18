// src/test/java/com/automationCalculator/Steps/AccessibilitySteps.java
package com.automationCalculator.Steps;

import com.automationCalculator.accessibility.AxeChecks;
import com.automationCalculator.support.ScenarioName;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;

import static com.automationCalculator.Driver.Setup.driver;

public class AccessibilitySteps {

  // Fails the scenario if violations found; writes JSON first.
  @Then("the page has no WCAG AA accessibility violations")
  public void assertNoWcagAA() {
    WebDriver d = driver;
    AxeChecks.check(d, ScenarioName.safe(), /*failOnViolation=*/true);
  }

  // Optional: run axe and only record (do not fail). Handy mid-scenario.
  @Then("capture accessibility snapshot")
  public void captureSnapshot() {
    WebDriver d = driver;
    AxeChecks.check(d, ScenarioName.safe() + "_snapshot", /*failOnViolation=*/false);
  }

  // Optional: labeled version if you want multiple axe runs in one scenario
  @Then("capture accessibility snapshot labeled {string}")
  public void captureSnapshotLabeled(String label) {
    WebDriver d = driver;
    String name = ScenarioName.safe() + "_" + label.replaceAll("\\W+","_");
    AxeChecks.check(d, name, /*failOnViolation=*/false);
  }
}
