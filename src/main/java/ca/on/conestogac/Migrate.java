package ca.on.conestogac;

import com.googlecode.flyway.core.Flyway;

public class Migrate {

	public static void main(String[] args) {
        // Create the Flyway instance
        Flyway flyway = new Flyway();

        // Point it to the database
        String sDir = "";
        if(System.getenv("OPENSHIFT_DATA_DIR") != null){
        	sDir = System.getenv("OPENSHIFT_DATA_DIR");
        }
        String sUrl = String.format("jdbc:derby:%sMyDbTest;create=true", sDir);
        System.out.println(sUrl);
        flyway.setDataSource(sUrl, null, null);

        // Start the migration
        flyway.migrate();
    }

}
