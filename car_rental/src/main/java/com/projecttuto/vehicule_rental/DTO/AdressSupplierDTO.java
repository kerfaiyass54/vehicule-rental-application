package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class AdressSupplierDTO {

    private Long idAdress;
    private String road;
    private int number;
    private String location;
}
