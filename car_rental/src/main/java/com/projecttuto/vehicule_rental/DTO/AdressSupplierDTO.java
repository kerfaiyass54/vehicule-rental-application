package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@NoArgsConstructor
@ToString
public class AdressSupplierDTO {

    private Long idAdress;
    private String road;
    private int number;
    private String location;
}
