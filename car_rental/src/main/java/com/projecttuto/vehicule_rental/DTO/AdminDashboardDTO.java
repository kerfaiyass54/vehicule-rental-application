package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminDashboardDTO {

    private long totalClients;

    private long totalSuppliers;

    private long totalRepairs;

    private long totalLocations;

    private long totalVehicles;

    private long totalBuyings;

    private long totalSubscriptions;

    private long totalTickets;

    private long totalDemands;

    private long activeRepairs;

}