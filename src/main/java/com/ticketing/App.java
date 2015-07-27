package com.ticketing;

import java.io.*;
import spark.Request;
import spark.Response;
import spark.Route;
import com.caucho.quercus.QuercusEngine;

 
import static spark.Spark.*;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        PHPRenderer php = new PHPRenderer();
        get("/", (request, response) -> {
            return php.render("views/test.php", "test");
        });
        get("/info", (request, response) -> {
            return php.render("views/info.php", "info");
        });
    }
}
