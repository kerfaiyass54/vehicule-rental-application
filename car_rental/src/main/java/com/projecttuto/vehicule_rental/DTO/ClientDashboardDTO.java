package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientDashboardDTO {

    private String clientName;

    private double budget;

    private int totalBuyings;

    private int activeBuyings;

    private int totalTickets;

    private int pendingTickets;

    private int completedTickets;

    private boolean subscribed;

    private String subscriptionType;
}