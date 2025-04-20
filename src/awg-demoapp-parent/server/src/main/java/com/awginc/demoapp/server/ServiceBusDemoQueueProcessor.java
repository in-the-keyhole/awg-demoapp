package com.awginc.demoapp.server;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.*;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusErrorContext;
import com.azure.messaging.servicebus.ServiceBusException;
import com.azure.messaging.servicebus.ServiceBusProcessorClient;
import com.azure.messaging.servicebus.ServiceBusReceivedMessageContext;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;

@Service
public class ServiceBusDemoQueueProcessor {

    private final Logger logger = LoggerFactory.getLogger(ServiceBusDemoQueueProcessor.class);

    private final ServiceBusAdministrationClient admin;
    private final ServiceBusClientBuilder client;
    private ServiceBusProcessorClient processor;

    @Autowired
    public ServiceBusDemoQueueProcessor(ServiceBusAdministrationClient admin, ServiceBusClientBuilder client) {
        this.admin = admin;
        this.client = client;
    }

    /**
     * Invoked when the application is ready.
     * 
     * Starts the queue processor.
     * 
     * @param event
     */
    @EventListener
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) throws Exception {
        if (admin.getQueueExists("demo_queue") == false) {
            logger.info("Creating 'demo_queue'.");
            admin.createQueue("demo_queue");
        }

        processor = client
                .processor()
                .queueName("demo_queue")
                .receiveMode(ServiceBusReceiveMode.PEEK_LOCK)
                .disableAutoComplete()
                .processMessage(processMessage)
                .processError(processError)
                .buildProcessorClient();

        processor.start();
    }

    /**
     * Invoked when a message is received on the queue.
     */
    Consumer<ServiceBusReceivedMessageContext> processMessage = context -> {
        final var message = context.getMessage();
        final var content = new String(message.getBody().toBytes(), StandardCharsets.UTF_8);
        logger.info("Received message: '{}'", content);
        context.complete();
    };

    /**
     * Received when an error occurs.
     */
    Consumer<ServiceBusErrorContext> processError = errorContext -> {
        if (errorContext.getException() instanceof ServiceBusException) {
            var exception = (ServiceBusException) errorContext.getException();
            logger.error("Error source: %s, reason %s%n", errorContext.getErrorSource(), exception.getReason());
        } else {
            logger.error("Error occurred: %s%n", errorContext.getException());
        }
    };

    /**
     * Invoked when the context is closed.
     * 
     * Stops the queue processor.
     * 
     * @param event
     */
    @EventListener
    public void onApplicationEvent(@NonNull ContextClosedEvent event) {
        if (processor != null) {
            try {
                logger.info("Shutting down ServiceBusDemoQueueProcessor.");
                processor.stop();
                processor.close();
                processor = null;
            } catch (Exception e) {

            }

        }
    }

}
