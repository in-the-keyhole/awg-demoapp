package com.awginc.demoapp.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClientBuilder;

@Configuration
public class ServiceBusConfig  {
    
    @Value("${conf.servicebus.endpoint}")
    private String endpoint;

    public ServiceBusAdministrationClient admin() {
        return new ServiceBusAdministrationClientBuilder()
            .credential(endpoint, new DefaultAzureCredentialBuilder().build())
            .buildClient();
    }

    public ServiceBusClientBuilder client() {
        return new ServiceBusClientBuilder()
            .credential(endpoint, new DefaultAzureCredentialBuilder().build());
    }

}
