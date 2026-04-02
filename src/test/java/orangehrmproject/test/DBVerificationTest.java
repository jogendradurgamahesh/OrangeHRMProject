package orangehrmproject.test;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.base.BaseClass;
import com.orangehrm.pages.HomePage;
import com.orangehrm.pages.LoginPage;
import com.orangehrm.utilities.DBConnection;
import com.orangehrm.utilities.DataProviders;
import com.orangehrm.utilities.ExtentManager;

public class DBVerificationTest extends BaseClass {

	private LoginPage loginPage;
	private HomePage homePage;
	
	@BeforeMethod
	public void setUpPages() {
		loginPage=new LoginPage(getDriver());
		homePage=new HomePage(getDriver());
	}
	
	
	@Test(dataProvider = "empVerification",dataProviderClass = DataProviders.class)
	public void verifyEmpNameFromDb(String empId,String empName) {
		
		//calling softassertion from baseClass
		SoftAssert softAssert=getSoftAssert();
		
		ExtentManager.logStep("Logging with credentials");
		loginPage.login(prop.getProperty("username"), prop.getProperty("password"));
		
		ExtentManager.logStep("Click on PIM TAB");
		homePage.clickOnPimTab();
		
		ExtentManager.logStep("Serach for an employee");
//		homePage.empSearch("Prince");
		homePage.empSearch(empName);
		
		ExtentManager.logStep("Get the emp name from db");
//		String employee_id="2"; empId
		String employee_id=empId; 
		
		//Fetch the data into map
		Map<String,String>empDetails= DBConnection.getEmployeeDetails(employee_id);
	    String emplFirst=empDetails.get("first_name");
	    String emplMiddleName =empDetails.get("middle_name");
	    String emplLast=empDetails.get("last_name");
	    
	    
	    String emplFirstAndMiddleName =(emplFirst+" "+emplMiddleName).trim();
	    
	    //validation for first name
	    ExtentManager.logStep("verify the firstname of emp");
//	   Assert.assertTrue(homePage.verifyFirstname(emplFirstAndMiddleName), "First name is not matching");
	    softAssert.assertTrue(homePage.verifyFirstname(emplFirstAndMiddleName), "First name is not matching");
	    
	   //validation for last name
	    ExtentManager.logStep("verify the lastname of emp");
//	    Assert.assertTrue(homePage.verifyLastname(emplLast), "Last name is not matching");
	    softAssert.assertTrue(homePage.verifyLastname(emplLast), "Last name is not matching");
	    
	    


	    ExtentManager.logStep("DB Validation completed");
	    
	    softAssert.assertAll();
	    
	}
	
	
	
	
}
