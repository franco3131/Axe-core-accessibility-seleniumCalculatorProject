// src/test/java/com/automationCalculator/Steps/AccessibilitySteps.java
package com.automationCalculator.Steps;

import com.automationCalculator.accessibility.AxeChecks;
import com.automationCalculator.support.ScenarioMeta;
import io.cucumber.java.en.Then;
import org.openqa.selenium.WebDriver;

import static com.automationCalculator.Driver.Setup.driver;

public class AccessibilitySteps {

  @Then("the page has no WCAG AA accessibility violations")
  public void assertNoWcagAA() {
    WebDriver d = driver;
    AxeChecks.check(d, ScenarioMeta.label(), /*failOnViolation=*/true);
  }

  // Optional: mid-scenario snapshots
  @Then("capture accessibility snapshot labeled {string}")
  public void captureSnapshotLabeled(String label) {
    WebDriver d = driver;
    String name = ScenarioMeta.label() + "__" + label.replaceAll("\\W+","_");
    AxeChecks.check(d, name, /*failOnViolation=*/false);
  }
}
