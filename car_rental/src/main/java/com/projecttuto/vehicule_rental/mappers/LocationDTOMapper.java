package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.LocationDTO;
import com.projecttuto.vehicule_rental.entities.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationDTOMapper {

    public LocationDTO mapToDTO(Location location) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setName(location.getName());
        locationDTO.setCountry(location.getCountry());
        locationDTO.setIdLoc(location.getIdLoc());
        return locationDTO;
    }
}
