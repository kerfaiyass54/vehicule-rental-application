package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.events.UserProvisioningEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class AccountProvisioningPublisher {
    private final KafkaTemplate<String, UserProvisioningEvent> kafka;
    private final String topic;
    private final String password;

    public AccountProvisioningPublisher(
            KafkaTemplate<String, UserProvisioningEvent> kafka,
            @Value("${account.provisioning.topic:keycloak-user-provisioning}") String topic,
            @Value("${keycloak.sync.default-password:123456}") String password) {
        this.kafka = kafka; this.topic = topic; this.password = password;
    }

    public void publish(String name, String email, String role) {
        String username = name.trim().toLowerCase().replaceAll("[^a-z0-9._-]", "_");
        kafka.send(topic, email, new UserProvisioningEvent(username, name, name, email, password, role));
    }
}
