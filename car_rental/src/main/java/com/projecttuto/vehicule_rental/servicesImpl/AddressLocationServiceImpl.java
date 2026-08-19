package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.exception.ResourceNotFoundException;
import com.projecttuto.vehicule_rental.repositories.AddressRepository;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.services.AddressLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressLocationServiceImpl implements AddressLocationService {

    private final AddressRepository addressRepository;
    private final LocationRepository locationRepository;

    @Override
    public int getAddressesPerLocation(String locationName) {

        Location location = locationRepository.findLocationByName(locationName);

        if (location == null) {
            throw new ResourceNotFoundException(
                    "Location not found: " + locationName
            );
        }

        return addressRepository
                .findAddressesByLocation(location)
                .size();
    }
}