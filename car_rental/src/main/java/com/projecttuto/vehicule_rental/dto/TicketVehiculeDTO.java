package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.Transmission;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketVehiculeDTO {

    private String vehiculeName;

    private String brand;

    private Transmission transmission;

    private String supplierName;
}