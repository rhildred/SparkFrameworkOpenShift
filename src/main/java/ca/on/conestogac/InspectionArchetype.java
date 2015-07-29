package ca.on.conestogac;

import org.json.simple.*;
import java.sql.*;
import java.util.*;
import javax.lang.model.*;

/**
 * archetype for inspections or uses of equipment that has its own user defined attributes
 *
 * @author rhildred
 *
 */
public class InspectionArchetype {
	/**
	 * where the parsed JSON will go
	 */
	private JSONObject oInput = null;

	/**
	 *
	 * @param sInput	a string of JSON that defines the new Archetype
	 */
	public InspectionArchetype(String sInput)
	{
		oInput = (JSONObject)JSONValue.parse(sInput);
	}

	/**
	 * create the metadata as well as the data table for a new archetype
	 *
	 * @throws Exception
	 */
	public void save() throws Exception
	{
		//these are external resources so we want to finalize them
		Connection connection = null;
        PreparedStatement oStmt = null, oStmtAttribute = null;
        ResultSet generatedKeys = null;

        // need to make sure name is a valid identifier to prevent SQL injection and to generate SQL that works
        String sName = (String) oInput.get("name");
		if(!SourceVersion.isName(sName))
			throw new Exception("name " + sName + " is not a valid java identifier");
		try{

			// need to do this in a database transaction as 3 tables are affected
			connection = OpenShiftDerbySource.getConnection();
			connection.setAutoCommit(false);

			// now we insert into DispClass getting the insert id into nID
        	oStmt = connection.prepareStatement("INSERT INTO DispClass(name) VALUES(?)", Statement.RETURN_GENERATED_KEYS);
        	oStmt.setString(1, sName);
            int affectedRows = oStmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating DispClass failed, no rows affected.");
            }
            Long nID = 0L;
            generatedKeys = oStmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                nID = generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating DispClass failed, no generated key obtained.");
            }
            oStmt.close();

            // build SQL to insert attributes
			oStmtAttribute = connection.prepareStatement("INSERT INTO DispAttribute(idDispClass, name, SQLType, formType) VALUES(?, ?, ?, ?)");

			// build the SQL for the new table
			String sSQL = "CREATE TABLE ";
			sSQL += sName + "(\nid" + sName + " INT NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),";
			@SuppressWarnings("unchecked")
			List<Map<String, String>> aAttributes = (List<Map<String, String>>)oInput.get("archetypeAttributes");
	        for(Map<String, String> oAttribute : aAttributes){

	        	// add attribute to create table SQL
	        	String sAttributeName = (String)oAttribute.get("name");

	        	// make sure name is a valid identifier
	        	if(!SourceVersion.isName(sAttributeName))
	    			throw new Exception("name " + sAttributeName + " is not a valid java identifier");

	        	// make sure that type is valid SQL type
	        	String sSQLType = ((String)oAttribute.get("SQLType")).toUpperCase();
	        	if(!sSQLType.matches("INT") &&
	        			!sSQLType.matches("DATE") &&
	        			!sSQLType.matches("VARCHAR[(][0-9]{1,5}[)]"))
	        			throw new Exception("sqlType " + sSQLType + " is not a valid SQLType");
	        	sSQL += "\n" + sAttributeName + " " + sSQLType + ",";

	        	// insert attribute
            	oStmtAttribute.setLong(1, nID);
            	oStmtAttribute.setString(2, (String)oAttribute.get("name"));
            	oStmtAttribute.setString(3, (String)oAttribute.get("SQLType"));
            	oStmtAttribute.setString(4, (String)oAttribute.get("formType"));
            	affectedRows = oStmtAttribute.executeUpdate();
                if (affectedRows == 0) {
                    throw new SQLException("Creating DispAttribute failed, no rows affected.");
                }

	        }
	        sSQL += "\nCONSTRAINT " + sName + "_primary_key PRIMARY KEY (id" + sName + "))";
	        oStmt = connection.prepareStatement(sSQL);
	        oStmt.execute();
			connection.commit();
		}catch(Exception e){
			connection.rollback();
			throw(e);
		}finally{
			//free up non gc'd external resources
            if (oStmt != null) try { oStmt.close(); } catch (SQLException logOrIgnore) {}
            if (connection != null) try { connection.close(); } catch (SQLException logOrIgnore) {}
		}
	}

	/**
	 * in a database transaction delete the data table as well as meta data based on the archetype name
	 * @throws Exception
	 */
	public void delete() throws Exception
	{
		// these are external resources that need to be closed
		Connection delConnection = null;
		PreparedStatement oStmt = null;
		ResultSet rs = null;
		try{
			//get idDispClass
			delConnection = OpenShiftDerbySource.getConnection();
			delConnection.setAutoCommit(false);
			String sName = (String) oInput.get("name");
			oStmt = delConnection.prepareStatement("SELECT idDispClass FROM DispClass WHERE name = ?");
			oStmt.setString(1, sName);
			rs = oStmt.executeQuery();
			rs.next();
			long idDispClass = rs.getLong(1);
			rs.close();
			oStmt.close();

			// now we delete from dispAtributes could conceivably have a DispClass with no dispattributes so we don't check oStmt.executeUpdate
			oStmt = delConnection.prepareStatement("DELETE FROM DispAttribute WHERE idDispClass = ?");
			oStmt.setLong(1, idDispClass);
            oStmt.executeUpdate();
            oStmt.close();

            //now we delete from dispclass
            oStmt = delConnection.prepareStatement("DELETE FROM DispClass WHERE idDispClass = ?");
			oStmt.setLong(1, idDispClass);
            int affectedRows = oStmt.executeUpdate();
            if (affectedRows == 0) {
            	// we would expect to delete at least 1 here
                throw new SQLException("Deleting DispClass failed, no rows affected.");
            }

            //now we drop table ... there should be no way to get here without having a valid java identifier
            oStmt.close();
            oStmt = delConnection.prepareStatement("DROP TABLE " + sName);
            oStmt.executeUpdate();
			delConnection.commit();
		}catch(Exception e){
			//fail silently because we want to keep on creating a new one in the test
			delConnection.rollback();
			e.printStackTrace();
		}finally{
			//free up non gc'd external resources
			if (rs != null) try { rs.close(); } catch (SQLException logOrIgnore) {}
            if (oStmt != null) try { oStmt.close(); } catch (SQLException logOrIgnore) {}
            if (delConnection != null) try { delConnection.close(); } catch (SQLException logOrIgnore) {}
		}
	}
}
