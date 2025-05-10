package com.awginc.storemaster.api;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Random;

public class DemoRestController {

    public static final String[] NAMES = new String[]{"Paul", "John", "Ringo", "George"};
    private static final Random r = new Random();

    @Autowired
    private Environment env;

    @GetMapping("helloWorld")
    public String helloWorld() {
        return "Hello World!!";
    }

    @GetMapping("name")
    public String getName(HttpServletResponse response) {
        response.addHeader("k8s-host", env.getProperty("HOSTNAME"));
        return NAMES[r.ints(0, NAMES.length).limit(1).findFirst().getAsInt()];
    }

}
