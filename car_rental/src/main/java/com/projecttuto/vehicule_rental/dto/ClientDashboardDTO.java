package com.projecttuto.vehicule_rental.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientDashboardDTO {

    private String clientName;

    private double budget;

    private Long totalBuyings;

    private Long activeBuyings;

    private Long totalTickets;

    private Long pendingTickets;

    private Long completedTickets;

    private boolean subscribed;

    private String subscriptionType;
}