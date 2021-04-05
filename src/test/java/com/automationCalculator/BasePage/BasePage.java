package com.automationCalculator.BasePage;
import com.automationCalculator.Driver.Setup;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class BasePage {
	public WebDriver driver;
	public BasePage(){
		this.driver=Setup.driver;
		
	}
	
	//Wait Wrapper Method
    public void waitVisibility(WebElement element) {
    	System.out.println(element);
    	WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(element));
    }
    
    public void goToUrl(String url) throws Exception{
    	  driver.get(url);
    }
 
  

}
