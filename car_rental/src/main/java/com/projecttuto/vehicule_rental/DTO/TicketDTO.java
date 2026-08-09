package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.RepairDemandStatus;
import com.projecttuto.vehicule_rental.enums.TicketType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor

public class TicketDTO {
    private Long idTicket;
    private TicketType type;
    private Instant dateInsert;
    private RepairDemandStatus status;
    private float tarif;
}
