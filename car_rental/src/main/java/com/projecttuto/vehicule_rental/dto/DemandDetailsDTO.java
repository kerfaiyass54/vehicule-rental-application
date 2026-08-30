package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.enums.DemandType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandDetailsDTO {

    private Long id;

    private DemandType type;

    private Instant date;

    private ConfirmStatus status;

    private Integer estimatedTime;

    private Long ticketId;

    private String supplierEmail;

    private String vehiculeName;
}