package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.AdressStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AdressDTO {

    private Long idAdress;
    private String road;
    private int number;
    private String location;
    private String supplierEmail;
    private AdressStatus adressStatus;
}
