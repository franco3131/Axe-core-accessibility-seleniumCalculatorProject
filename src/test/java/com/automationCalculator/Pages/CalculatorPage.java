package com.automationCalculator.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

import com.automationCalculator.BasePage.BasePage;

public class CalculatorPage extends BasePage {
	

	@FindBy(className="equal")
	private WebElement equal;
	@FindBy(className="plus")
	private WebElement plus;
	@FindBy(className="subtract")
	private WebElement subtract;
	@FindBy(xpath="//button[contains(text(),'X')]")
	private WebElement multiply;
	@FindBy(xpath="//button[contains(text(),'/')]")
	private WebElement divide;
	@FindBy(css="body > div > div.well.output")
	private WebElement outputBox;


	public CalculatorPage() {
		super();
		PageFactory.initElements(this.driver, this);
	}
	
	/**
	 * @author franco
	 * return true of false whether calculator page is displayed
	 */
	
	public boolean isCalculatorPageDisplayed() {
		try {
			waitVisibility(outputBox);
			return this.outputBox.isDisplayed();
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	/**
	 * @author franco
	 * This returns the output of calculator
	 */
	
	public String output() throws Exception{
		waitVisibility(this.outputBox);
		return this.outputBox.getText();
	}
	
	/**
	 * @author franco
	 * click on plus button
	 */
	
	public void clickPlus() throws Exception{
		waitVisibility(this.plus);
		this.plus.click();	
		
	}
	
	/**
	 * @author franco
	 * click on subtract button
	 */
	
	public void clickSubtract() throws Exception{
		waitVisibility(this.subtract);
		this.subtract.click();	
		
	}
	
	/**
	 * @author franco
	 * click on multiply button
	 */
	
	public void clickMultiply() throws Exception{
		waitVisibility(this.multiply);
		this.multiply.click();	
		
	}
	
	/**
	 * @author franco
	 * click on divide button
	 */
	
	public void clickDivide() throws Exception{
		waitVisibility(this.divide);
		this.divide.click();	
		
	}
	
	/**
	 * @author franco
	 * click on equal button
	 */
	
	public void clickEquals() throws Exception{
		waitVisibility(this.equal);
		this.equal.click();
	}
	
	/**
	 * @author franco
	 * click on any number button
	 */
	
	public void clickNumberButton(int number) throws Exception{
		WebElement button=driver.findElement(By.xpath("//button[contains(text(),'"+number+"')]"));
		waitVisibility(button);
		button.click();	
		
	}
	
	
	
	

}
