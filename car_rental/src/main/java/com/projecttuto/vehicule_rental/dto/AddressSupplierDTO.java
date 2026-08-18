package com.projecttuto.vehicule_rental.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressSupplierDTO {

    private Long idAddress;
    private String road;
    private Integer number;
    private String location;
}