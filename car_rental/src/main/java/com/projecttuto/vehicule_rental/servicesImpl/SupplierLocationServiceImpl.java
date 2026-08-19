package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.entities.Address;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.repositories.AddressRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.SupplierLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierLocationServiceImpl implements SupplierLocationService {

    private final SupplierRepository supplierRepository;
    private final AddressRepository addressRepository;

    @Override
    public List<String> getLocations(String email) {

        Supplier supplier = findSupplierByEmail(email);

        List<Location> locations = getSupplierLocations(supplier);

        return locations.stream()
                .map(Location::getLocationName)
                .toList();
    }

    @Override
    public List<String> getCountries(String email) {

        Supplier supplier = findSupplierByEmail(email);

        List<Location> locations = getSupplierLocations(supplier);

        return locations.stream()
                .map(Location::getCountry)
                .toList();
    }

    @Override
    public List<LocationDTO> getLocations(
            String email,
            int size,
            int page) {

        Supplier supplier = findSupplierByEmail(email);

        if (supplier.getAddresses() == null) {
            return List.of();
        }

        return supplier.getAddresses()
                .stream()
                .map(Address::getLocation)
                .distinct()
                .skip((long) page * size)
                .limit(size)
                .map(this::mapToDTO)
                .toList();
    }

    private Supplier findSupplierByEmail(String email) {

        Supplier supplier =
                supplierRepository.findSupplierByEmail(email);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        return supplier;
    }

    private List<Location> getSupplierLocations(
            Supplier supplier) {

        return addressRepository
                .findAddressesBySupplier(supplier)
                .stream()
                .map(Address::getLocation)
                .toList();
    }

    private LocationDTO mapToDTO(Location location) {

        LocationDTO dto = new LocationDTO();

        dto.setIdLoc(location.getIdLocation());
        dto.setName(location.getLocationName());
        dto.setCountry(location.getCountry());
        dto.setPosition(location.getPosition());

        return dto;
    }
}