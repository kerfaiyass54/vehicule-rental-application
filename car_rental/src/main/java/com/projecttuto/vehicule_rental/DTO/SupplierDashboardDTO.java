package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierDashboardDTO {

    private String supplierName;

    private int totalVehicles;

    private int totalBuyings;

    private int activeBuyings;

    private int totalSubscriptions;

    private int totalDemands;

    private int approvedDemands;

    private int refusedDemands;

    private int pendingDemands;
}