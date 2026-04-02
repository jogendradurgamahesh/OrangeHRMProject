package com.orangehrm.utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class ApiUtility {

	//Method to send the GET Request
	public static Response sendGetRequest(String endPoint) {
	  return RestAssured.get(endPoint);
	}
	
	//method to send the post reqest
	public static Response sendPostRequest(String endPoint,String payload) {
	return	RestAssured.given().header("Content-type","application/json")
		           .body(payload)
		           .post();
		
	}
	
	//Method to vliadate the Response status
	public static boolean validateStatusCode(Response res,int statusCode) {
		 return res.getStatusCode()==statusCode;
	}
	
	//method to extract values from JSON response
	public static String getJsonValue(Response res,String value) {
		return res.jsonPath().getString(value);
	}
	
	
	
//	//Method to send the put request
//	public static Response sendPutRequest(String endPoint,String payLoad) {
//		return RestAssured.given();
//	}
//	
	
}
