package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.enums.StatusRepair;
import com.projecttuto.vehicule_rental.enums.TypeTicket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class RepairTicketDTO {

    private Long idTicket;

    private String clientName;

    private String vehiculeName;

    private TypeTicket ticketType;

    private String description;

    private Instant dateTicket;

    private StatusRepair ticketStatus;

    private String demandType;

    private Integer estimatedTime;

    private String supplierName;

    private ConfirmStatus demandStatus;
}