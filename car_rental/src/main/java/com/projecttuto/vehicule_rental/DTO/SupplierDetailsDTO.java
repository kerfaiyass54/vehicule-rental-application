package com.projecttuto.vehicule_rental.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SupplierDetailsDTO {

    private String suppName;
    private String nationality;
    private String email;
    private int experience;

}
