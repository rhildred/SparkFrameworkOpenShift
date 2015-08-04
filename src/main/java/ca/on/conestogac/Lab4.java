package ca.on.conestogac;

import java.sql.*;

public class Lab4 {
	public static void main(String[] args) {
		//set these to be null so that we can finally close them
        final Connection connection = OpenShiftDataSource.getConnection("jdbc:mysql://localhost:3306/kanbanpomodoro?user=root");
        try{
        	//make a stmt from my SQL
        	Statement oStmt = connection.createStatement();
        	String sSQL = "SELECT * FROM tests";
        	ResultSet oRs = oStmt.executeQuery(sSQL);
        	System.out.println(ResultSetValue.toJsonString(oRs));
            oRs.close();
        }catch(Exception e){
        	e.printStackTrace();
        }finally{
        	try{
        		if(connection != null) connection.close();
        	}catch(Exception e){
        		e.printStackTrace();
        	}
        }
	}
}
