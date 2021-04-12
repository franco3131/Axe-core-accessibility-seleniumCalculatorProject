package cucumberTest;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;


@RunWith(Cucumber.class)
@CucumberOptions(
         features={"src/test/resources/Features/Calculator.feature"},
         glue={"com.automationCalculator"},
         plugin = {"json:target/cucumber.json"},

         strict=true
         
         
  )

  public class Runner {
 
}