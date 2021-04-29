package com.automationCalculator.Steps;

import com.automationCalculator.Pages.CalculatorPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;

import static org.junit.Assert.assertEquals;



public class CalculatorSteps {
	CalculatorPage calculator=new CalculatorPage();
	@Given("I go to the calculator page")
	public void i_go_to_the_calculator_page() throws Exception {
		calculator.goToUrl("https://rawcdn.githack.com/franco3131/Wood-Calculator-Javascript/a1175acf8a268e506e2fd6b39dbb0a6156b6c29e/Calculator/HTML/calculator.html");

	}

	@And("The calculator page is displayed")
	public void theCalculatorPageIsDisplayed() throws Exception{
		calculator.isCalculatorPageDisplayed();
		//assertEquals(calculator.isCalculatorPageDisplayed(),true);
	}
	
	@And("I click on button {int}")
	public void iClickOnButton(int button) throws Exception{
		calculator.clickNumberButton(button);
	}
	
	@And("I click on the equal button")
	public void iClickOnTheEqualButton() throws Exception{
		calculator.clickEquals();
	}
	
	@And("I click on the plus button")
	public void iClickOnThePlusButton() throws Exception{
		calculator.clickPlus();
	}
	
	@And("I click on the subtract button")
	public void iClickOnTheSubtractButton() throws Exception{
		calculator.clickSubtract();
	}
	
	@And("I click on the multiply button")
	public void iClickOnTheMultiplyButton() throws Exception{
		calculator.clickMultiply();
	}
	
	@And("I click on the division button")
	public void iClickOnTheDivisionButton() throws Exception{
		calculator.clickDivide();
	}
	
	@Then("The calculator outputs {int}")
	public void theCalculatorOutput(int output) throws Exception{
		
		int outputInteger=Integer.valueOf(calculator.output());
		assertEquals(outputInteger,output);
	}
	
	@Then("The calculator outputs the decimal {double}")
	public void theCalculatorOutput(double output) throws Exception{
		double outputInteger=Double.valueOf(calculator.output());
		assertEquals(outputInteger,output,0.0);
	}
	

}
