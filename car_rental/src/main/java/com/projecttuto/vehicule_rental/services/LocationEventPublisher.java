package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.events.LocationEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
public class LocationEventPublisher {

    private final KafkaTemplate<String, LocationEvent> kafkaTemplate;
    private final String topic;

    public LocationEventPublisher(
            KafkaTemplate<String, LocationEvent> kafkaTemplate,
            @Value("${KAFKA_LOCATION_EVENTS_TOPIC:car-rental.location-events}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void publish(
            String eventType,
            String entityType,
            Long entityId,
            Location previousLocation,
            Location newLocation) {
        if (entityId == null) {
            log.warn("Skipping {} event for {} with no persisted ID", eventType, entityType);
            return;
        }

        LocationEvent event = new LocationEvent(
                UUID.randomUUID().toString(),
                eventType,
                entityType,
                entityId.toString(),
                Instant.now(),
                newLocation == null ? null : stringId(newLocation.getIdLocation()),
                previousLocation == null ? null : stringId(previousLocation.getIdLocation()),
                newLocation == null ? null : stringId(newLocation.getIdLocation()),
                newLocation == null ? null : newLocation.getLocationName(),
                newLocation == null ? null : newLocation.getCountry(),
                latitude(newLocation),
                longitude(newLocation)
        );

        send(event);
    }

    private void send(LocationEvent event) {
        Runnable send = () -> {
            try {
                kafkaTemplate.send(topic, event.entityType() + ":" + event.entityId(), event)
                        .whenComplete((result, error) -> {
                            if (error != null) {
                                log.error("Failed to publish {} event for {} {}", event.eventType(), event.entityType(), event.entityId(), error);
                            } else {
                                log.debug("Published {} event for {} {}", event.eventType(), event.entityType(), event.entityId());
                            }
                        });
            } catch (RuntimeException exception) {
                log.error("Could not submit {} event for {} {} to Kafka", event.eventType(), event.entityType(), event.entityId(), exception);
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

    private String stringId(Long id) {
        return id == null ? null : id.toString();
    }

    private Double latitude(Location location) {
        double[] coordinates = coordinates(location);
        return coordinates == null ? null : coordinates[0];
    }

    private Double longitude(Location location) {
        double[] coordinates = coordinates(location);
        return coordinates == null ? null : coordinates[1];
    }

    private double[] coordinates(Location location) {
        if (location == null || location.getPosition() == null) {
            return null;
        }
        try {
            String[] parts = location.getPosition().split(",");
            if (parts.length != 2) {
                return null;
            }
            return new double[]{Double.parseDouble(parts[0].trim()), Double.parseDouble(parts[1].trim())};
        } catch (NumberFormatException exception) {
            log.debug("Location {} has a non-coordinate position", location.getIdLocation());
            return null;
        }
    }
}
