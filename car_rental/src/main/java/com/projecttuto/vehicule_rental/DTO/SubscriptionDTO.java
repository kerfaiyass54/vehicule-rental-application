package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.SubscriptionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SubscriptionDTO {

    private long idSubscrip;
    private SubscriptionType type;
    private Instant dateStart;
    private double price;
    private int reduce;
    private String supplierName;
    private String clientName;
}
