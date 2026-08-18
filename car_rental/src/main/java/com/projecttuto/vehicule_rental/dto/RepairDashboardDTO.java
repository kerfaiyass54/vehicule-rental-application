package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepairDashboardDTO {

    @NotBlank
    private String repairName;

    @NotBlank
    private String location;

    private int totalTickets;

    private int pendingTickets;

    private int acceptedTickets;

    private int completedTickets;

    private int activeRepairs;

    private int completedRepairs;

    private int cancelledRepairs;

    private int totalDemands;

    private int pendingDemands;

    private int acceptedDemands;

    private int rejectedDemands;
}