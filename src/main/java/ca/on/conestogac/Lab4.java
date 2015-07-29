package ca.on.conestogac;

import java.sql.*;

public class Lab4 {
	public static void main(String[] args) {
		//set these to be null so that we can finally close them
        Connection connection = null;
        Statement oStmt = null;
        try{
        	//make a stmt from my SQL
        	connection = OpenShiftDerbySource.getConnection();
        	oStmt = connection.createStatement();
        	String sSQL = "SELECT * FROM PERSON";
        	ResultSet oRs = oStmt.executeQuery(sSQL);
        	System.out.println(ResultSetValue.toJsonString(oRs));
            oRs.close();
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	try{
        		if(oStmt != null) oStmt.close();
        		if(connection != null) connection.close();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
	}
}
