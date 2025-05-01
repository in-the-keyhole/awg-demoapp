package com.awginc.demoapp.server;

import com.azure.messaging.servicebus.*;
import com.azure.messaging.servicebus.administration.ServiceBusAdministrationClient;
import com.azure.messaging.servicebus.models.ServiceBusReceiveMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

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
    final Consumer<ServiceBusReceivedMessageContext> processMessage = context -> {
        final var message = context.getMessage();
        final var content = new String(message.getBody().toBytes(), StandardCharsets.UTF_8);
        logger.info("Received message: '{}'", content);
        context.complete();
    };

    /**
     * Received when an error occurs.
     */
    final Consumer<ServiceBusErrorContext> processError = errorContext -> {
        if (errorContext.getException() instanceof ServiceBusException exception) {
            logger.error("Error source: {}, reason {}", errorContext.getErrorSource(), exception.getReason());
        } else {
            logger.error("Error occurred: {}", errorContext.getException());
        }
    };

    /**
     * Invoked when the context is closed.
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
