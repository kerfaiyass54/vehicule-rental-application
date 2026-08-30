package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.enums.DemandType;
import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.TicketType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class RepairTicketDTO {

    @NotNull
    @Positive
    private Long idTicket;

    @NotBlank
    private String clientName;

    @NotBlank
    private String vehiculeName;

    @NotNull
    private TicketType ticketType;

    @NotBlank
    private String description;

    @NotNull
    private Instant dateTicket;

    @NotNull
    private RepairDemandStatus ticketStatus;

    @NotBlank
    private DemandType demandType;

    @NotNull
    @Positive
    private Integer estimatedTime;

    @NotBlank
    private String supplierName;

    @NotNull
    private ConfirmStatus demandStatus;
}