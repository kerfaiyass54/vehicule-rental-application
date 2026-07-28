package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierAdminDTO {

    private Long id;

    private String suppName;

    private String email;

    private String nationality;

    private int experience;

    private String role;
}