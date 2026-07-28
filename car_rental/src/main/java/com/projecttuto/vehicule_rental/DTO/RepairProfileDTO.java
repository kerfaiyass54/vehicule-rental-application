package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepairProfileDTO {

    private Long idRepair;

    private String nameRepair;

    private String email;

    private String role;

    private String locationName;

    private String country;

    private String position;
}