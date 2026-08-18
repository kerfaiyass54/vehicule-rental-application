package com.projecttuto.vehicule_rental.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class AdminDTO {
//here
    private Long idAdmin;
    private String adminName;
    private String email;
    private String password;
}
