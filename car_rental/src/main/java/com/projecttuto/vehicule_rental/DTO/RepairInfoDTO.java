package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.RepairStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;
import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class RepairInfoDTO {
    private long idInfo;
    private RepairStatus repairStatus;
    private Instant dateStart;
}
