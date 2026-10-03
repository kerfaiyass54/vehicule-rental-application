package com.projecttuto.vehicule_rental.config;

import com.projecttuto.vehicule_rental.events.LocationEvent;
import com.projecttuto.vehicule_rental.events.BudgetEvent;
import com.projecttuto.vehicule_rental.events.UserProvisioningEvent;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfiguration {

    @Bean
    public ProducerFactory<String, UserProvisioningEvent> accountProducerFactory(Environment environment) {
        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                environment.getProperty("KAFKA_BOOTSTRAP_SERVERS",
                        environment.getProperty("spring.kafka.bootstrap-servers", "localhost:9194")));
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, UserProvisioningEvent> accountKafkaTemplate(
            ProducerFactory<String, UserProvisioningEvent> factory) {
        return new KafkaTemplate<>(factory);
    }

    @Bean
    public ProducerFactory<String, LocationEvent> locationEventProducerFactory(Environment environment) {
        String bootstrapServers = environment.getProperty(
                "KAFKA_BOOTSTRAP_SERVERS",
                environment.getProperty("spring.kafka.bootstrap-servers", "localhost:9194")
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean
    public KafkaTemplate<String, LocationEvent> kafkaTemplate(
            ProducerFactory<String, LocationEvent> locationEventProducerFactory) {
        return new KafkaTemplate<>(locationEventProducerFactory);
    }

    @Bean
    public ProducerFactory<String, BudgetEvent> budgetEventProducerFactory(Environment environment) {
        String bootstrapServers = environment.getProperty(
                "KAFKA_BOOTSTRAP_SERVERS",
                environment.getProperty("spring.kafka.bootstrap-servers", "localhost:9194")
        );

        Map<String, Object> properties = new HashMap<>();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        properties.put(ProducerConfig.ACKS_CONFIG, "all");
        properties.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, true);

        return new DefaultKafkaProducerFactory<>(properties);
    }

    @Bean("budgetKafkaTemplate")
    public KafkaTemplate<String, BudgetEvent> budgetKafkaTemplate(
            ProducerFactory<String, BudgetEvent> budgetEventProducerFactory) {
        return new KafkaTemplate<>(budgetEventProducerFactory);
    }
}
