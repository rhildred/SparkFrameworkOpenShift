package ca.on.conestogac;

import java.sql.*;

public class OpenShiftDerbySource {
	public static Connection getConnection() throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException{
    	//create the connection to our derby database that is updated with the migrations
    	Class.forName("org.apache.derby.jdbc.EmbeddedDriver").newInstance();
        String sDir = "";
        if(System.getenv("OPENSHIFT_DATA_DIR") != null){
        	sDir = System.getenv("OPENSHIFT_DATA_DIR");
        }
        String sUrl = String.format("jdbc:derby:%sMyDbTest", sDir);
    	Connection connection = DriverManager.getConnection( sUrl);
    	return connection;
	}
}
