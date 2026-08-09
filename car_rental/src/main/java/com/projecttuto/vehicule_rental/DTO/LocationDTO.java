package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
public class LocationDTO {

    private Long idLoc;
    private String name;
    private String country;
    private String position;


}
