package com.orangehrm.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.orangehrm.base.BaseClass;

public class DBConnection {

	private static final String DB_URL="jdbc:mysql://localhost:3307/orangehrm";
	private static final String DB_USERNAME="root";
	private static final String DB_PASSWORD="";
	
	public static final Logger logger=BaseClass.logger;
	
	public static Connection getDBConnection() {
		try {
			logger.info("starting DB connection");
			Connection conn= DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
			logger.info("Connection successfull");
			return conn;
		} catch (SQLException e) {
			logger.error("Error while establishing dB connection");
			e.printStackTrace();
			return null;
		}
		
	}
	
	
	//get  employees details from db and store it in map
	public static Map<String,String> getEmployeeDetails(String employee_id){
		String query="select emp_firstname, emp_middle_name,emp_lastname from hs_hr_employee where emp_number="+employee_id;
		
		Map<String,String>empDetails=new HashMap<>();
		
		try(Connection conn=getDBConnection();
				Statement stat=conn.createStatement();
				ResultSet res=stat.executeQuery(query))
		{
			logger.info("Executing query "+query);
			if(res.next()) {
			String first_name=res.getString("emp_firstname");
			String middleName = res.getString("emp_middle_name");
			String last_name=res.getString("emp_lastname");
			//store in a map
			empDetails.put("first_name", first_name);
			empDetails.put("middle_name", middleName!=null? middleName:"");
			empDetails.put("last_name", last_name);
			
			logger.info("Query executed successfully");
			logger.info("Emp data fetched successfully "+empDetails);
			}
			else {
				logger.error("Details not found");
			}
		}
			catch(Exception e) {
				logger.info("Error while executing query");
				e.printStackTrace();
			}
			return empDetails;
		}


	

}
	

