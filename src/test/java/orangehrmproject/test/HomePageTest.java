package orangehrmproject.test;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class HomePageTest extends BaseClass {
	
	private LoginPage loginPage;
	private HomePage homepage;
	
	@BeforeMethod
	public void setUp() {
		loginPage=	new LoginPage(getDriver());
		homepage=new HomePage(getDriver());
	}
	
	@Test(dataProvider = "ValidLoginData",dataProviderClass = DataProviders.class)
	public void verifyOrangeHRMLogo(String username,String password) {
		//ExtentManager.startTest("Home Page Verify Logo Test"); //--This has been implemented in TestListener
		ExtentManager.logStep("Navigating to Login Page entering username and password");
//		loginPage.login("Admin", "admin123");
		loginPage.login(username, password);
		ExtentManager.logStep("Verifying Logo is visible or not");
		Assert.assertTrue(homepage.verifyOrangeHRMLogo(),"Logo not visible");
		 ExtentManager.logStep("Validation Successful");
		 ExtentManager.logStep("Logged out Successfully!");
	}

}
