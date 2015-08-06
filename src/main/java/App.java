import java.sql.*;

import com.salesucation.sparkphp.PHPRenderer;

import ca.on.conestogac.*;
import static spark.Spark.*;

/**
 * Hello world!
 *
 */
public class App
{
    private static final String IP_ADDRESS = System.getenv("OPENSHIFT_DIY_IP") != null ? System.getenv("OPENSHIFT_DIY_IP") : "localhost";
    private static final int PORT = System.getenv("OPENSHIFT_DIY_PORT") != null ? Integer.parseInt(System.getenv("OPENSHIFT_DIY_PORT")) : 4567;
    public static void main(String[] args) {
        setIpAddress(IP_ADDRESS);
        setPort(PORT);
        externalStaticFileLocation(System.getProperty("user.dir") + "/public/");
        PHPRenderer php = new PHPRenderer();
        php.setViewDir("views/");
        final Connection connection = OpenShiftDataSource.getConnection("jdbc:mysql://localhost:3306/kanbanpomodoro?user=root");
        try{
            get("/", (request, response) -> {
                return php.render("test.php", "test");
            });
            get("/info", (request, response) -> {
                return php.render("info.php", "info");
            });
            get("/tests", (request, response) -> {
                //make a stmt from my SQL
                String rc = "";
                try{
                    Statement oStmt = connection.createStatement();
                    String sSQL = "SELECT * FROM tests";
                    ResultSet oRs = oStmt.executeQuery(sSQL);
                    rc = ResultSetValue.toJsonString(oRs);
                    oRs.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                return rc;
            });
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
}