package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.RepairStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class RepairInfoDTO {

    private Long idRepairInfo;

    private String vehiculeName;

    private String clientName;

    private String repairName;

    private Instant dateStart;

    private RepairStatus repairStatus;
}