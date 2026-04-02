package orangehrmproject.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class LoginPageTest extends BaseClass{

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod //using before method because create obj before every testCase 
	public void setUpPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	
	@Test(dataProvider = "ValidLoginData",dataProviderClass = DataProviders.class)
	public void verifyValidLoginTest(String username,String password) {
	 
	//ExtentManager.startTest("Valid login Test"); ////--This has been implemented in TestListener
	 System.out.println("Running testMethod1 on thread: " + Thread.currentThread().getId());
	 ExtentManager.logStep("Navigating to Login Page entering username and password");
//	 loginPage.login("Admin","admin123");	
	 loginPage.login(username, password);
	 ExtentManager.logStep("Verifying Admin Tab is visible or not");
	 Assert.assertTrue(homePage.isAdminTabVisible(),"Admin tab should be visible after successful login ");
	 ExtentManager.logStep("Validation Successful");
	 homePage.clickOnLogout();
	 ExtentManager.logStep("Logged out Successfully!");
	staticWait(4); //will wait couple of sec after logout
	}
	
	@Test(dataProvider = "InvalidLoginData",dataProviderClass = DataProviders.class)
	public void invalidLoginTest(String username,String password) {
		//ExtentManager.startTest("In-Valid login Test");  //--This has been implemented in TestListener
		System.out.println("Running testMethod2 on thread: " + Thread.currentThread().getId());
		 ExtentManager.logStep("Navigating to Login Page entering username and password");
//		loginPage.login("Admin", "admin1234");
		 loginPage.login(username, password);
		String expResult="Invalid credentials";
		Assert.assertTrue(loginPage.verifyErrorMessage(expResult),"Test failed:Invalid Msg");
		ExtentManager.logStep("Validation Successful");
		ExtentManager.logStep("Logged out Successfully!");
	}
	
	
}
