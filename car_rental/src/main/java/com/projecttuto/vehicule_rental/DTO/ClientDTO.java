package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class ClientDTO {
    private Long idClient;
    private String nameClient;
    private String nationality;
    private double budget;
    private String locationName;
    private String email;
    private String role;


}
