package com.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.orangehrm.actiondriver.ActionDriver;
import com.orangehrm.base.BaseClass;

public class HomePage {
	
	private ActionDriver actionDriver;
	
	//*[text()='Logout']
	//define locators using class
	private By adminTab=By.xpath("//span[text()='Admin']");
	private By userIdButton=By.className("oxd-userdropdown-name");
	private By logoutBtn=By.xpath("//*[text()='Logout']");
    private By orangeHRMLogo=By.xpath("//div[@class='oxd-brand-banner']/img");
    private By pimTab=By.xpath("//span[text()='PIM']");
    private By employeeSearch=By.xpath("//label[text()='Employee Name']/parent::div/following-sibling::div/div/div/input");
    private By searchButton=By.xpath("//button[@type='submit']");
    private By firstName=By.xpath(" //div[@class='oxd-table-card']/div/div[3]");
    private By lastName=By.xpath(" //div[@class='oxd-table-card']/div/div[4]");
    
 /*   //initilaize the Action Driver by passing WebDriver instance
    public HomePage(WebDriver driver) {
    	this.actionDriver=new ActionDriver(driver);
    }   */
    
    public HomePage(WebDriver driver) {
    	this.actionDriver=BaseClass.getActionDriver();
    }
    
    //method to verify if admin tab is visible
    public boolean isAdminTabVisible() {
    	return actionDriver.isDisplayed(adminTab);
    }
    
    //verify OrangeHRMLogo
    public boolean verifyOrangeHRMLogo() {
    	return actionDriver.isDisplayed(orangeHRMLogo);
    }
    
    
    //method to navigate to pim tab
    public void clickOnPimTab() {
    	actionDriver.click(pimTab);
    }
    
    //Employee Search
    public void empSearch(String value) {
    	actionDriver.enterText(employeeSearch, value);
    	actionDriver.click(searchButton);
    	actionDriver.scrollToElement(firstName);
    }

//    public void empSearch(String value) {
//
//        actionDriver.enterText(employeeSearch, value);
//
//        // 🔴 Wait 2 seconds for dropdown (important)
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        // 🔴 Click exact matching suggestion
//        By suggestion = By.xpath("//div[@role='listbox']//span[text()='" + value + "']");
//        actionDriver.click(suggestion);
//
//        actionDriver.click(searchButton);
//    	actionDriver.scrollToElement(firstName);
//    }
    
    
//    public boolean verifyEmployee(String fullName){
//
//        By empName = By.xpath("//div[@class='oxd-table-card']//div[text()='"+fullName+"']");
//        return actionDriver.isDisplayed(empName);
//    }
    
    //verify firstname
    public boolean verifyFirstname(String empFirstFromDb) {
    	return actionDriver.compareText(firstName, empFirstFromDb);
    	
    }
    
    //verify laststname
    public boolean verifyLastname(String empLastFromDb) {
    	return actionDriver.compareText(lastName, empLastFromDb);
    	
    }
    
    
    //click on logout and userIdBtn
    public void clickOnLogout() {
    	actionDriver.click(userIdButton);
    	actionDriver.click(logoutBtn);
    }
    
    
    
}
