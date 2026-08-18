package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
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
public class DemandResponseDTO {

    @NotNull
    private Long idDemand;

    @NotBlank
    private String type;

    @NotNull
    private Instant dateAsk;

    @Positive
    private int estimatedTime;

    @NotNull
    private ConfirmStatus status;

    @NotBlank
    private String vehiculeName;

    @NotBlank
    private String repairName;

    @NotBlank
    private String clientName;

    @NotNull
    private Long ticketId;
}