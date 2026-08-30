package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.TicketType;
import lombok.*;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailsDTO {

    private Long idTicket;

    private TicketType type;

    private String description;

    private Instant dateInsert;

    private RepairDemandStatus status;

    private Double tariff;

    private String clientEmail;

    private String vehiculeName;

}