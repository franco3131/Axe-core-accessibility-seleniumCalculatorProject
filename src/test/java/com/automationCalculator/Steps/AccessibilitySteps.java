
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

}
