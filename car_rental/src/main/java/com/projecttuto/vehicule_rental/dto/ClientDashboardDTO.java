package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientDashboardDTO {

    @NotBlank
    private String clientName;

    @DecimalMin(value = "0.0")
    private double budget;

    private int totalBuyings;

    private int activeBuyings;

    private int totalTickets;

    private int pendingTickets;

    private int completedTickets;

    private boolean subscribed;

    private String subscriptionType;
}