package com.projecttuto.vehicule_rental.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepairDashboardDTO {

    
    private String repairName;

    
    private String location;

    private Long totalTickets;

    private Long pendingTickets;

    private Long acceptedTickets;

    private Long completedTickets;

    private Long activeRepairs;

    private Long completedRepairs;

    private Long cancelledRepairs;

    private Long totalDemands;

    private Long pendingDemands;

    private Long acceptedDemands;

    private Long rejectedDemands;
}