package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.events.RecommendationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class RecommendationGateway {

    private final RestClient restClient;
    private final ConcurrentHashMap<String, RecommendationResponse> latest = new ConcurrentHashMap<>();

    public RecommendationGateway(
            @Value("${recommendation.ai.url:http://localhost:8090}") String aiUrl) {
        this.restClient = RestClient.builder().baseUrl(aiUrl).build();
    }

    public void request(String email) {
        restClient.post()
                .uri("/recommendations")
                .body(new RecommendationRequestBody(email))
                .retrieve()
                .toBodilessEntity();
    }

    public RecommendationResponse latest(String email) {
        return latest.get(email.toLowerCase());
    }

    @KafkaListener(
            topics = "${KAFKA_RECOMMENDATION_RESULTS_TOPIC:recommendation_results}",
            containerFactory = "recommendationKafkaListenerContainerFactory")
    public void consume(RecommendationResponse response) {
        if (response != null && response.email() != null) {
            latest.put(response.email().toLowerCase(), response);
        }
    }

    private record RecommendationRequestBody(String email) {}
}
