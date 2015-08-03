package com.ticketing;

import static spark.Spark.*;

public class App {
    private static final String IP_ADDRESS = System.getenv("OPENSHIFT_DIY_IP") != null ? System.getenv("OPENSHIFT_DIY_IP") : "localhost";
    private static final int PORT = System.getenv("OPENSHIFT_DIY_PORT") != null ? Integer.parseInt(System.getenv("OPENSHIFT_DIY_PORT")) : 4567;

    public static void main(String[] args) {
        setIpAddress(IP_ADDRESS);
        setPort(PORT);
        get("/", (req, res) -> "Hello World");
    }
}
