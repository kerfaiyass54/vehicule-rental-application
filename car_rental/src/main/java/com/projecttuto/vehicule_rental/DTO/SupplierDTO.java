package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SupplierDTO {
    private long idSupp;
    private String suppName;
    private String nationality;
    private String email;
    private String pass;
    private String role;


}
