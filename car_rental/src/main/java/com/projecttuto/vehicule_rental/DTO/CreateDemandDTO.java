package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateDemandDTO {

    private Long ticketId;

    private String repairEmail;

    private String supplierName;

    private String type;

    private int estimatedTime;
}