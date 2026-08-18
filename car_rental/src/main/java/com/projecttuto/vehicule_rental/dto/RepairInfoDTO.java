package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.RepairStatus;
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
public class RepairInfoDTO {

    @NotNull
    @Positive
    private Long idRepairInfo;

    @NotBlank
    private String vehiculeName;

    @NotBlank
    private String clientName;

    @NotBlank
    private String repairName;

    @NotNull
    private Instant dateStart;

    @NotNull
    private RepairStatus repairStatus;
}