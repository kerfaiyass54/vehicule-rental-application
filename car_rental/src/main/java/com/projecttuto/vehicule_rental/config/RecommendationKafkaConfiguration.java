package com.projecttuto.vehicule_rental.config;

import com.projecttuto.vehicule_rental.events.RecommendationResponse;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Configuration
public class RecommendationKafkaConfiguration {

    @Bean
    public ConsumerFactory<String, RecommendationResponse> recommendationConsumerFactory(
            Environment environment) {
        String bootstrapServers = environment.getProperty(
                "KAFKA_BOOTSTRAP_SERVERS",
                environment.getProperty("spring.kafka.bootstrap-servers", "localhost:9194"));

        Map<String, Object> properties = new HashMap<>();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        // A fresh group replays retained results on every restart so the cache
        // is rebuilt from Kafka instead of depending on process memory.
        properties.put(ConsumerConfig.GROUP_ID_CONFIG,
                "spring-recommendation-results-" + UUID.randomUUID());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        properties.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);

        JsonDeserializer<RecommendationResponse> deserializer =
                new JsonDeserializer<>(RecommendationResponse.class);
        deserializer.addTrustedPackages("com.projecttuto.vehicule_rental.events");
        return new DefaultKafkaConsumerFactory<>(
                properties, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, RecommendationResponse>
    recommendationKafkaListenerContainerFactory(
            ConsumerFactory<String, RecommendationResponse> consumerFactory) {
        ConcurrentKafkaListenerContainerFactory<String, RecommendationResponse> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        return factory;
    }
}
