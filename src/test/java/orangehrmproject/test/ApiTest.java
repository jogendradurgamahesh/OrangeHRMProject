package orangehrmproject.test;

import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.orangehrm.utilities.ApiUtility;
import com.orangehrm.utilities.ExtentManager;
import com.orangehrm.utilities.RetryAnalyzer;

import io.restassured.response.Response;

public class ApiTest {

//	@Test(retryAnalyzer = RetryAnalyzer.class)
	@Test
	public void verifyGetUsersApi(){
		
		SoftAssert softAssert=new SoftAssert();
	
		//1.Define EndPoint
	String endPoint="https://jsonplaceholder.typicode.com/users/1";
	ExtentManager.logStep("API Endpoint: " + endPoint);
	
	//2.Sending GET req
	ExtentManager.logStep("Sending GET request to the api");
	Response res= ApiUtility.sendGetRequest(endPoint);
	
	//3.Validate status code
	ExtentManager.logStep("Validating API status code");
	boolean isStatusCodeValid= ApiUtility.validateStatusCode(res, 200);
	
//	Assert.assertTrue(isStatusCodeValid,"Status code is not as expected");
	softAssert.assertTrue(isStatusCodeValid,"Status code is not as expected");
	
	if(isStatusCodeValid) {
		ExtentManager.logStepValidationForApi("Status code Validation passed!!");
	}
	else {
		ExtentManager.logFailureAPI("Status code is faileed");
	}
	
	//4.validate username
	ExtentManager.logStep("Validating response body for username");
	String username=ApiUtility.getJsonValue(res, "username");
	boolean isUserNameValid= "Bret".equals(username);
//	Assert.assertTrue(isUserNameValid,"Username is not valid");
	softAssert.assertTrue(isUserNameValid,"Username is not valid");
	
	if(isUserNameValid) {
		ExtentManager.logStepValidationForApi("Username vaildation is passed");
	}
	else{
		ExtentManager.logFailureAPI("Username failed");
	}
	
	//4.validate email
		ExtentManager.logStep("Validating response body for Email");
		String email=ApiUtility.getJsonValue(res, "email");
		boolean isEmailValid= "Sincere@april.biz".equals(email);
		//Assert.assertTrue(isEmailValid,"Email is not valid");
		softAssert.assertTrue(isEmailValid,"Email is not valid");
		
		if(isEmailValid) {
			ExtentManager.logStepValidationForApi("Email vaildation is passed");
		}
		else{
			ExtentManager.logFailureAPI("Email vaildation failed");
		}
	
		softAssert.assertAll();
	}
	
}
