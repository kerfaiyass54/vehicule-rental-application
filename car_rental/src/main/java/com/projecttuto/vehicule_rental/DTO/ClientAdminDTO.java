package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientAdminDTO {

    private Long id;

    private String nameClient;

    private String email;

    private String nationality;

    private double budget;

    private Long locationId;

    private String locationName;
}