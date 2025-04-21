package com.awginc.demoapp.server;

import com.azure.spring.messaging.implementation.annotation.EnableAzureMessaging;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAzureMessaging
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
