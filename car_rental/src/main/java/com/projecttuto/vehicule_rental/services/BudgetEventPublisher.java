package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.events.BudgetEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class BudgetEventPublisher {

    private final KafkaTemplate<String, BudgetEvent> kafkaTemplate;
    private final String topic;

    public BudgetEventPublisher(
            @Qualifier("budgetKafkaTemplate") KafkaTemplate<String, BudgetEvent> kafkaTemplate,
            @Value("${KAFKA_BUDGET_EVENTS_TOPIC:budget_events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(String eventType, Long clientId, String email,
                        Double previousBudget, Double budget) {
        if (clientId == null || email == null || budget == null) {
            log.warn("Skipping {} event for client with incomplete budget data", eventType);
            return;
        }

        BudgetEvent event = new BudgetEvent(
                UUID.randomUUID().toString(),
                eventType,
                "client",
                clientId.toString(),
                email,
                budget,
                previousBudget,
                Instant.now()
        );

        Runnable send = () -> {
            try {
                kafkaTemplate.send(topic, "client:" + clientId, event)
                        .whenComplete((result, error) -> {
                            if (error != null) {
                                log.error("Failed to publish {} event for client {}",
                                        event.eventType(), event.entityId(), error);
                            } else {
                                log.debug("Published {} event for client {}",
                                        event.eventType(), event.entityId());
                            }
                        });
            } catch (RuntimeException exception) {
                log.error("Could not submit {} event for client {} to Kafka",
                        event.eventType(), event.entityId(), exception);
            }
        };

        if (TransactionSynchronizationManager.isSynchronizationActive()
                && TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    send.run();
                }
            });
        } else {
            send.run();
        }
    }
}
