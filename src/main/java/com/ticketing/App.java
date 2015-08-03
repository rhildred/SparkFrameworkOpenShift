package com.ticketing;

import java.io.*;
import com.caucho.quercus.QuercusEngine;

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
        PHPRenderer php = new PHPRenderer();
        get("/", (request, response) -> {
            return php.render("views/test.php", "test");
        });
        get("/info", (request, response) -> {
            return php.render("views/info.php", "info");
        });
    }
}
