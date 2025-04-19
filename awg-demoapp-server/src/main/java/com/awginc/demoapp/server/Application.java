package com.awginc.demoapp.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.azure.spring.messaging.implementation.annotation.EnableAzureMessaging;

@SpringBootApplication
@EnableAzureMessaging
public class Application {

	public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
	}

}
