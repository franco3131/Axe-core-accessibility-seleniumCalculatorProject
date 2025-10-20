package com.automationCalculator.Steps;

import com.automationCalculator.Pages.CalculatorPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class CalculatorSteps {

    private final CalculatorPage calculator = new CalculatorPage();

    @Given("I go to the calculator page")
    public void iGoToTheCalculatorPage() throws Exception {
        calculator.goToUrl(
            "https://rawcdn.githack.com/franco3131/Wood-Calculator-Javascript/a1175acf8a268e506e2fd6b39dbb0a6156b6c29e/Calculator/HTML/calculator.html"
        );
    }

    @And("The calculator page is displayed")
    public void theCalculatorPageIsDisplayed() throws Exception {
        assertTrue(calculator.isCalculatorPageDisplayed(), "Calculator page should be visible");
    }

    @And("I click on button {int}")
    public void iClickOnButton(int button) throws Exception {
        calculator.clickNumberButton(button);
    }

    @And("I click on the equal button")
    public void iClickOnTheEqualButton() throws Exception {
        calculator.clickEquals();
    }

    @And("I click on the plus button")
    public void iClickOnThePlusButton() throws Exception {
        calculator.clickPlus();
    }

    @And("I click on the subtract button")
    public void iClickOnTheSubtractButton() throws Exception {
        calculator.clickSubtract();
    }

    @And("I click on the multiply button")
    public void iClickOnTheMultiplyButton() throws Exception {
        calculator.clickMultiply();
    }

    @And("I click on the division button")
    public void iClickOnTheDivisionButton() throws Exception {
        calculator.clickDivide();
    }

    @Then("The calculator outputs {int}")
    public void theCalculatorOutputsInt(int expected) throws Exception {
        int actual = Integer.parseInt(calculator.output().trim());
        assertEquals(actual, expected, "Integer result mismatch");
    }

    @Then("The calculator outputs the decimal {double}")
    public void theCalculatorOutputsDecimal(double expected) throws Exception {
        double actual = Double.parseDouble(calculator.output().trim());
        assertEquals(actual, expected, 0.0, "Decimal result mismatch");
    }
}
