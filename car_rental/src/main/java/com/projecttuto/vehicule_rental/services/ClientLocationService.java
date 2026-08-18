package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.LocationDTO;

public interface ClientLocationService {
    LocationDTO updateClientLocation(String clientEmail, LocationDTO locationDTO);

}
