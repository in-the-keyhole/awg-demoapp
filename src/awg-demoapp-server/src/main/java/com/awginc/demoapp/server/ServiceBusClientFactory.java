package com.awginc.demoapp.server;

import java.net.URI;
import java.net.URISyntaxException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClientBuilder;

@Configuration
public class ServiceBusClientFactory  {
    
    @Value("${conf.servicebus.endpoint}")
    private String endpoint;

    /**
     * Gets the fully qualified namespace from the configured endpoint. The endpoint may come in as a https:// or sb:// URI.
     * @return The fully qualified namespace of the Service Bus.
     * @throws Exception
     */
    String getFullyQualifiedNamespace() throws Exception {
        if (endpoint == null && endpoint.length() == 0) {
            throw new Exception("Empty Service Bus endpoint.");
        }

        try {
            final var uri = new URI(endpoint);

            if (uri.getScheme().equals("http")) {
                return uri.getHost();
            } else if (uri.getScheme().equals("https")) {
                return uri.getHost();
            } else if (uri.getScheme().equals("sb")) {
                return uri.getHost();
            } else {
                throw new Exception("Unknown scheme '" + uri.getScheme() + "' for Service Bus endpoint.");
            }
        } catch (URISyntaxException e) {
            return endpoint;
        }
    }

    @Bean
    public ServiceBusAdministrationClient createServiceBusAdminClient() throws Exception {
        if (endpoint == null && endpoint.length() == 0) {
            throw new Exception("Empty Service Bus endpoint.");
        }

        return new ServiceBusAdministrationClientBuilder()
            .endpoint(endpoint)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();
    }

    @Bean
    public ServiceBusClientBuilder createServiceBusClientBuilder() throws Exception {
        return new ServiceBusClientBuilder()
            .fullyQualifiedNamespace(getFullyQualifiedNamespace())
            .credential(new DefaultAzureCredentialBuilder().build());
    }

}
