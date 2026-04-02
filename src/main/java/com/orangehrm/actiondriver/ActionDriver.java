package com.orangehrm.actiondriver;

import java.time.Duration;

import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class ActionDriver {

	private WebDriver driver;
	private WebDriverWait wait;
	public static final Logger logger=BaseClass.logger;

	public ActionDriver(WebDriver driver) {
		this.driver=driver;
		int explicitWait=Integer.parseInt(BaseClass.getProp().getProperty("explicitWait"));
		this.wait=new WebDriverWait(driver, Duration.ofSeconds(explicitWait));
//		System.out.println("Wd instances created");
		logger.info("Wd instances created");
	}
	
	//method to click an element
	public void click(By by) {
		String elementDes=getElementDesription(by);
		try {
			applyBorder(by, "green");
			waitForElementToBeClickable(by);
			driver.findElement(by).click();
			ExtentManager.logStep("Clicked an Element--->"+elementDes);
			logger.info("Element clicked---> "+elementDes);
		} catch (Exception e) {
			applyBorder(by, "red");
			System.out.println("unable to click Element"+e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "unable to click Element:", elementDes+"_unable to click element");
			logger.error("unable to click "+e.getMessage());
		}
	}
	
	//method to enter an input filed
	public void enterText(By by,String value) {
		try {
			waitElementsToBeVisible(by);
			applyBorder(by, "green");
//			driver.findElement(by).clear();
//			driver.findElement(by).sendKeys(value);
			WebElement element=driver.findElement(by); //to avoid code duplication-
			element.clear();
			element.sendKeys(value);
			logger.info("Entered text "+getElementDesription(by)+"----> "+value);
		} catch (Exception e) {
//			System.out.println("unable to enter the value "+e.getMessage());
			applyBorder(by, "red");
			logger.error("unable to enter the value "+e.getMessage());
		}
	}
	
	
	//method to get text from input field
	public String getText(By by) {
		try {
			waitElementsToBeVisible(by);
			applyBorder(by, "green");
			return driver.findElement(by).getText();
		} catch (Exception e) {
//			System.out.println("unable to get the text "+e.getMessage());
			applyBorder(by, "red");
			logger.error("unable to get the text "+e.getMessage());
			return "";
		}
		
	}
	
	
	//methods to comapre text ---change the return type
	public boolean compareText(By by,String expectedText) {
		try {
			waitElementsToBeVisible(by);
			String actualText=driver.findElement(by).getText();
			if(actualText.equals(expectedText)) {
				applyBorder(by, "green");
//				System.out.println("Texts are matching "+actualText+" equals "+expectedText);
				logger.info("Texts are matching "+actualText+" equals "+expectedText);
				ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Compare Text", "Text verified Successfully! "+actualText+" equals "+expectedText);
				return true;
			}else {
				applyBorder(by, "red");
				logger.error("Texts are not  matching "+actualText+" not equals "+expectedText);
				ExtentManager.logFailure(BaseClass.getDriver(), " Text Comparison Failed ", "Text comparison failed! "+actualText+" Not equals "+expectedText);
				return false;
			}
		} catch (Exception e) {
			logger.error("unable to compate texts "+e.getMessage());
		}
		return false;
	}
	
	
/*	//method to check if an element is displayed
	public boolean isDisplayed(By by) {
		try {
			waitElementsToBeVisible(by);
			boolean isDisplayed=driver.findElement(by).isDisplayed();
			if(isDisplayed) {
				System.out.println("Element displayed");
				return isDisplayed;
			}else {
				return isDisplayed;
				
			}
		} catch (Exception e) {
			System.out.println("Element not displayed "+e.getMessage());
			return false;
		}
		
	}  */
	
	//simplified method and remove redudanct methods
	public boolean isDisplayed(By by) {
		try {
			waitElementsToBeVisible(by);
			applyBorder(by, "green");
			logger.info("Element Displayed "+getElementDesription(by));
			ExtentManager.logStep("Element Displayed: "+getElementDesription(by));
			ExtentManager.logStepWithScreenshot(BaseClass.getDriver(), "Element is displayed: ", "Element is displayed: "+getElementDesription(by));
			return driver.findElement(by).isDisplayed();
		}
		catch(Exception e) {
			applyBorder(by, "red");
			logger.error("Element not displayed "+e.getMessage());
			ExtentManager.logFailure(BaseClass.getDriver(), "Element not  Displayed: ","Element not  Displayed" +getElementDesription(by));
			return false;
		}
	}
	
	//waitForPageLoad
	public void waitForPageLoad(int timeOutInSec) {
		try {
			wait.withTimeout(Duration.ofSeconds(timeOutInSec)).until(WebDriver -> ((JavascriptExecutor) WebDriver)
					.executeScript("return document.readyState").equals("complete"));
			logger.info("Page loaded successfully.");
		} catch (Exception e) {
			logger.error("Page did not load within " + timeOutInSec + " seconds. Exception: " + e.getMessage());
		}
	}
	
	
	//scroll to an element
	public void scrollToElement(By by) {
		try {
			applyBorder(by, "green");
			JavascriptExecutor js=(JavascriptExecutor) driver;
			WebElement element=driver.findElement(by);
			js.executeScript("arguments[0].scrollIntoView(true);", element);
		} catch (Exception e) {
			applyBorder(by, "red");
			logger.error("unable to locate element "+e.getMessage());
		}
	}
	
	
	//Wait For ElementToBeClickable
	private void waitForElementToBeClickable(By by) {
		try {
		wait.until(ExpectedConditions.elementToBeClickable(by));
	}
		catch(Exception e){
			logger.error("element is not clickable "+e.getMessage());
		}
	}
	
	
	//wait for elements to be visible
	private void waitElementsToBeVisible(By by) {
		try {
		wait.until(ExpectedConditions.visibilityOfElementLocated(by));
		}
		catch(Exception e){
			logger.error("element is not visible "+e.getMessage());
		}
	}
	
	
	
	//Method to get the description of an element
	public String getElementDesription(By locator) {
		//check for null driver or locator to avoid null pointer exp
		if(driver==null)
			return "Driver is null";
		if(locator==null) 
			return "Locator is null";
		
		//find element using locator
		try {
			WebElement element=driver.findElement(locator);
			 // WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			  
			  
			//Get Element Attribute
			String name=element.getDomAttribute("name");
			String id=element.getDomAttribute("id");
			String text=element.getText();
			String className=element.getDomAttribute("class");
			String placeHolder=element.getDomAttribute("placeholder");
			
			//return Description based on element attribute
			if(isNotEmpty(name)) {
				return "Element with name: "+name;
			}
			else if(isNotEmpty(id)) {
				return "Element with id: "+id;
			}
			else if(isNotEmpty(text)) {
				return "Element with text: "+truncate(text,40);
			}
			else if(isNotEmpty(className)) {
				return "Element with className: "+className;
			}
			else if(isNotEmpty(placeHolder)) {
				return "Element with placeHolder: "+placeHolder;
			}
		} catch (Exception e) {
//			logger.error("unable to describe the element");
			 logger.warn("Could not fetch element attributes for locator: " + locator);
		}
		return "Locator: " + locator.toString();
	}	
		
		
		//utility method to check if string is not null or empty
		private boolean isNotEmpty(String value) {
			return value!=null && !value.isEmpty();
		
		}
		
		//utitlity method to truncate long string
		private String truncate(String value,int maxLength) {
			if(value==null || value.length()<=maxLength) {
				return value;
			}
			return value.substring(0, maxLength)+"...";
		}
		
		
	//utility method to border an element
	public void applyBorder(By by,String color) {
		try {
			//Locate the element
			WebElement element= driver.findElement(by);
			//Apply the border
			String script="arguments[0].style.border='3px solid "+color+"'";
			JavascriptExecutor js=(JavascriptExecutor)driver;
			js.executeScript(script, element);
			logger.info("Applied border to the color "+color+" to element "+getElementDesription(by));
		} catch (Exception e) {
			logger.warn("unable to Apply border to an element "+getElementDesription(by),e);
		}
		
	}
	

}
