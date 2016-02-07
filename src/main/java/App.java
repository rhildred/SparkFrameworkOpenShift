import java.sql.*;

import com.salesucation.sparkphp.PHPRenderer;
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
        try{
            get("/", (request, response) -> {
                return php.render("test.php");
            });
            get("/info", (request, response) -> {
                return php.render("info.php");
            });
            get("/rocks/:name", (request, response) -> {
                return php.render("rocks.php", "{\"name\":\"" + request.params(":name") + "\"}");
            });
        }catch(Exception e){
        	e.printStackTrace();
        }
    }
}