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
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @author franco
	 * return true of false whether calculator page is displayed
	 * @return
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
	 * @return
	 */
	
	public String output() throws Exception{
		waitVisibility(this.outputBox);
		return this.outputBox.getText();
	}
	
	/**
	 * @author franco
	 * click on plus button
	 * @return
	 */
	
	public void clickPlus() throws Exception{
		waitVisibility(this.plus);
		this.plus.click();	
		
	}
	
	/**
	 * @author franco
	 * click on subtract button
	 * @return
	 */
	
	public void clickSubtract() throws Exception{
		waitVisibility(this.subtract);
		this.subtract.click();	
		
	}
	
	/**
	 * @author franco
	 * click on multiply button
	 * @return
	 */
	
	public void clickMultiply() throws Exception{
		waitVisibility(this.multiply);
		this.multiply.click();	
		
	}
	
	/**
	 * @author franco
	 * click on divide button
	 * @return
	 */
	
	public void clickDivide() throws Exception{
		waitVisibility(this.divide);
		this.divide.click();	
		
	}
	
	/**
	 * @author franco
	 * click on equal button
	 * @return
	 */
	
	public void clickEquals() throws Exception{
		waitVisibility(this.equal);
		this.equal.click();
	}
	
	/**
	 * @author franco
	 * click on any number button
	 * @return
	 */
	
	public void clickNumberButton(int number) throws Exception{
		WebElement button=driver.findElement(By.xpath("//button[contains(text(),'"+number+"')]"));
		waitVisibility(button);
		button.click();	
		
	}
	
	
	
	

}
