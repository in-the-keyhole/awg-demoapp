package com.awginc.demoapp.server;

import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusException;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessage;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;

@Service
public class ServiceBusDemoQueueProcessor implements ApplicationListener<ApplicationReadyEvent> {

    private final ServiceBusConfig config;
    private ServiceBusProcessorClient processor;

    @Autowired
    public ServiceBusDemoQueueProcessor(ServiceBusConfig config) {
        this.config = config;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        System.out.println("onApplicationEvent: ready");
        config.admin().createQueue("demo_queue");
        processor = config.client().processor().queueName("demo_queue").processMessage(processMessage).processError(processError).buildProcessorClient();
    }

    Consumer<ServiceBusReceivedMessageContext> processMessage = context -> {
        final ServiceBusReceivedMessage message = context.getMessage();
        System.out.println("Message received.");
        context.complete();
    };

    Consumer<ServiceBusErrorContext> processError = errorContext -> {
        if (errorContext.getException() instanceof ServiceBusException) {
            ServiceBusException exception = (ServiceBusException)errorContext.getException();
            System.out.printf("Error source: %s, reason %s%n", errorContext.getErrorSource(), exception.getReason());
        } else {
            System.out.printf("Error occurred: %s%n", errorContext.getException());
        }
    };

}
