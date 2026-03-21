package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.records.AiBehaviorRequest;
import com.projecttuto.vehicule_rental.records.AiResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AiClient {

    @Value("${suspicious.predict}")
    private String predictLink;

    private final RestTemplate restTemplate = new RestTemplate();

    public AiResult analyze(AiBehaviorRequest request) {
        return restTemplate.postForObject(
                predictLink,
                request,
                AiResult.class
        );
    }
}

