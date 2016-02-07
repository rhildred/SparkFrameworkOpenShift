package io.github.rhildred;

import java.sql.*;

import org.json.simple.*;

import java.util.*;
import java.io.*;

public class ResultSetValue {
	public static String toJsonString(ResultSet oRs) throws SQLException{
    	//get metadata about SQL, which could be anything
    	ResultSetMetaData oMd = oRs.getMetaData();
    	int nCols = oMd.getColumnCount();

    	//make a list to hold results
    	List<Map<String, String>> aResults = new LinkedList<Map<String, String>>();
        while(oRs.next()){

        	//for each row make a map
        	Map<String, String> oMap = new HashMap<String, String>();

        	//for each column (starting with 1!!!!!!) put a name value pair in the map
        	for(int n = 1; n <= nCols; n++){
        		oMap.put(oMd.getColumnLabel(n), oRs.getString(n));
        	}
        	aResults.add(oMap);
        }

		return JSONValue.toJSONString(aResults);
	}
	public static String toJsonString(InputStream inputStream)throws Exception{
		StringBuilder stringBuilder = new StringBuilder();
		BufferedReader bufferedReader = null;
		try {
			if (inputStream != null) {
				bufferedReader = new BufferedReader(
						new InputStreamReader(inputStream));
				char[] charBuffer = new char[128];
				int bytesRead = -1;
				while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
					stringBuilder.append(charBuffer, 0, bytesRead);
				}
			} else {
				stringBuilder.append("");
			}
		} catch (IOException ex) {
			throw ex;
		} finally {
			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException ex) {
					throw ex;
				}
			}
		}
		return stringBuilder.toString();

	}
}
