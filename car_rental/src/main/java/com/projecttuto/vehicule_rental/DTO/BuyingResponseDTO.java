package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.BuyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class BuyingResponseDTO {

    private Long idBuying;

    private String vehiculeName;

    private String clientName;

    private String clientEmail;

    private Instant dateBuy;

    private int period;

    private BuyStatus status;
}