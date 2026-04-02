package com.orangehrm.utilities;

import java.util.List;

import org.testng.annotations.DataProvider;

public class DataProviders {

	private static final String FILE_PATH=System.getProperty("user.dir")+"/src/test/resources/testData/testData.xlsx";

	@DataProvider(name="ValidLoginData")
	public static Object[][] validLoginData(){
		return getSheetData("ValidLoginData");
	}
	
	
	@DataProvider(name="InvalidLoginData")
	public static Object[][] invalidLoginData(){
		return getSheetData("InvalidLoginData");
	}
	
	@DataProvider(name="empVerification")
	public static Object[][] empVerification(){
		return getSheetData("empVerification");
	}
	
	
     private static Object[][] getSheetData(String sheetName){
     List<String[]>sheetData=ExcelReaderUtility.getSheetData(FILE_PATH, sheetName);
     Object[][] data=new Object[sheetData.size()][sheetData.get(0).length];
     
     for(int i=0;i<sheetData.size();i++) {
    	 data[i]=sheetData.get(i);
    	 
     }
     return data;
     
     }


}
