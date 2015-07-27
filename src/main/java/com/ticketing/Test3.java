package com.ticketing;

import java.util.HashMap;
import java.util.Map;


public class Test3
{
    public static void main(String[] args) {
        PHPRenderer php = new PHPRenderer();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("message", "Hello World!");
        System.out.println(php.render("views/test.php", "test", attributes));
    }
}