
package cucumberTest;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
  features = "src/test/resources/Features",
  glue = {"com.automationCalculator.Steps","com.automationCalculator.Driver"},
  plugin = {"pretty","json:target/cucumber.json"},
  monochrome = true
)
public class Runner extends AbstractTestNGCucumberTests {}

