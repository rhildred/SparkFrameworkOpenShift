
import com.googlecode.flyway.core.Flyway;
import ca.on.conestogac.*;

public class Migrate {

	public static void main(String[] args) {
        // Create the Flyway instance
        Flyway flyway = new Flyway();

        // Point it to the database
        String sUrl = OpenShiftDataSource.getConnectionString("jdbc:mysql://localhost:3306/ticketing?user=root");
        System.out.println(sUrl);
        flyway.setDataSource(sUrl, null, null);

        // Start the migration
        flyway.migrate();
    }

}
