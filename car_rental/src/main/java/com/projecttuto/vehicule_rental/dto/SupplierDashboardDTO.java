package com.projecttuto.vehicule_rental.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierDashboardDTO {


    private String supplierName;

    private Long totalVehicles;

    private Long totalBuyings;

    private Long activeBuyings;

    private Long totalSubscriptions;

    private Long totalDemands;

    private Long approvedDemands;

    private Long refusedDemands;

    private Long pendingDemands;
}