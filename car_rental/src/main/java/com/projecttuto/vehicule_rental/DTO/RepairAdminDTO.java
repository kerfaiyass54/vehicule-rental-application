package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepairAdminDTO {

    private Long id;

    private String nameRepair;

    private String email;

    private String role;

    private Long locationId;

    private String locationName;
}