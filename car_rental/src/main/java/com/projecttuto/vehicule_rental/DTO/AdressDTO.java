package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.AddressStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AddressDTO {

    private Long idAddress;
    private String road;
    private int number;
    private String location;
    private String supplierEmail;
    private AddressStatus adressStatus;
}
