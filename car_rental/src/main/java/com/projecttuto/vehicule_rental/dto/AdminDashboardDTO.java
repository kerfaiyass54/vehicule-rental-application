package com.projecttuto.vehicule_rental.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminDashboardDTO {

    private Long totalClients;
    private Long totalSuppliers;
    private Long totalRepairs;
    private Long totalLocations;
    private Long totalVehicles;
    private Long totalBuyings;
    private Long totalSubscriptions;
    private Long totalTickets;
    private Long totalDemands;
    private Long activeRepairs;
}