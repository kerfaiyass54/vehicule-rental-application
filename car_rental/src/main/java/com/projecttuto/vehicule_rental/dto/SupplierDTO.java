package com.projecttuto.vehicule_rental.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class SupplierDTO {
    private Long idSupp;
    private String suppName;
    private String nationality;
    private String email;
    private String pass;
    private String role;


}
