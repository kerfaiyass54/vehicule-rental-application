package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationAdminDTO {

    private Long id;

    private String name;

    private String country;

    private String position;
}