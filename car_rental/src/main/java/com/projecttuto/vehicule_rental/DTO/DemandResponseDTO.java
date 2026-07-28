package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class DemandResponseDTO {

    private Long idDemand;

    private String type;

    private Instant dateAsk;

    private int estimatedTime;

    private ConfirmStatus status;

    private String vehiculeName;

    private String repairName;

    private String clientName;

    private Long ticketId;
}