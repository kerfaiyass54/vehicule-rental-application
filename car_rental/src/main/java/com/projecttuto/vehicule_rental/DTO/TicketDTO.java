package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.StatusRepair;
import com.projecttuto.vehicule_rental.enums.TypeTicket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor

public class TicketDTO {
    private long idTicket;
    private TypeTicket type;
    private Instant dateInsert;
    private StatusRepair status;
    private float tarif;
}
