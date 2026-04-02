package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class LoginPage {

	private ActionDriver actionDriver;
	
	//Define locators using class
	private By userNameField=By.name("username");
	private By passwordField=By.cssSelector("input[type='password']");
	//private By loginButton=By.cssSelector("button[type='submit']");
	private By loginButton=By.xpath("//button[text()=' Login ']");
	private By errorMessage=By.xpath("//*[text()='Invalid credentials']");
	
/*	//initilaize the Action Driver by passing WebDriver instance
	public LoginPage(WebDriver driver) {
		this.actionDriver=new ActionDriver(driver);
	}  */
	

		public LoginPage(WebDriver driver) {
			this.actionDriver=BaseClass.getActionDriver();
		}
	
	
	//method to perform login
	public void login(String userName,String password) {
		actionDriver.enterText(userNameField, userName);
		actionDriver.enterText(passwordField, password);
		actionDriver.click(loginButton);
	}
	
	//method to check if error msg is displayed
	public boolean isErrorMsgDisplayed() {
		return actionDriver.isDisplayed(errorMessage);
	}
	
	//method to get text from error msg
	public String getErrorMessageText() {
		return actionDriver.getText(errorMessage);
	}
	
	//check if error is  correct or not
	public boolean verifyErrorMessage(String expectedError) {
		return actionDriver.compareText(errorMessage, expectedError);
	}
	
}
