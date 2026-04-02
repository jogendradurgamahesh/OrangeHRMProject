package orangehrmproject.test;

import org.testng.SkipException;
import org.testng.annotations.Test;

import com.orangehrm.base.BaseClass;
import com.orangehrm.utilities.ExtentManager;

public class DummyClass2 extends BaseClass {

	@Test
	public void dummyTest() {
//		ExtentManager.startTest("DummyTest1 Test"); //--This has been implemented in TestListener
	//	String title=driver.getTitle();
		String title=getDriver().getTitle(); 
		ExtentManager.logStep("verifying the title");
		assert title.equals("OrangeHRM"):"Test Failed-Title not matching";
		
		System.out.println("Test Passed-Title  matching");
		
		 ExtentManager.logStep("Validation Successful");
	}

}
