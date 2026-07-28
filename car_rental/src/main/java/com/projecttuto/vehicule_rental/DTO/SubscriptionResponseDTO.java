package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.SubscriptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class SubscriptionResponseDTO {

    private Long idSubscription;

    private String clientName;

    private String clientEmail;

    private SubscriptionType type;

    private Instant dateStart;

    private double price;

    private int reduce;
}