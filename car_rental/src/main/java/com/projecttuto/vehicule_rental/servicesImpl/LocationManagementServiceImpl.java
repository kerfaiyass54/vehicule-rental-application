package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.LocationAdminDTO;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.exception.ResourceNotFoundException;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.services.LocationManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationManagementServiceImpl implements LocationManagementService {

    private final LocationRepository locationRepository;

    @Override
    public List<String> getLocationsNames() {
        return locationRepository.findAll()
                .stream()
                .map(Location::getLocationName)
                .toList();
    }

    @Override
    public List<String> getCountries() {
        return locationRepository.findAll()
                .stream()
                .map(Location::getCountry)
                .toList();
    }

    @Override
    public List<String> getCitiesByCountry(String country) {
        return locationRepository.findLocationsByCountry(country)
                .stream()
                .map(Location::getLocationName)
                .toList();
    }

    @Override
    public LocationAdminDTO createLocation(LocationAdminDTO dto) {

        Location location = buildLocation(dto);

        Location savedLocation = locationRepository.save(location);

        return toDTO(savedLocation);
    }

    @Override
    public Page<LocationAdminDTO> getLocations(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return locationRepository.findAll(pageable)
                .map(this::toDTO);
    }

    @Override
    public LocationAdminDTO getLocation(Long id) {

        Location location = findLocationById(id);

        return toDTO(location);
    }

    @Override
    public LocationAdminDTO updateLocation(
            Long id,
            LocationAdminDTO dto) {

        Location location = findLocationById(id);

        updateLocationFields(location, dto);

        Location savedLocation = locationRepository.save(location);

        return toDTO(savedLocation);
    }

    @Override
    public void deleteLocation(Long id) {

        Location location = findLocationById(id);

        locationRepository.delete(location);
    }

    /**
     * Finds a location or throws a domain-specific exception.
     */
    private Location findLocationById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found with id: " + id
                        )
                );
    }

    /**
     * Creates a Location entity from the request DTO.
     */
    private Location buildLocation(LocationAdminDTO dto) {

        Location location = new Location();

        location.setLocationName(dto.getName());
        location.setCountry(dto.getCountry());
        location.setPosition(dto.getPosition());

        return location;
    }

    /**
     * Updates an existing Location entity.
     */
    private void updateLocationFields(
            Location location,
            LocationAdminDTO dto) {

        location.setLocationName(dto.getName());
        location.setCountry(dto.getCountry());
        location.setPosition(dto.getPosition());
    }

    /**
     * Converts a Location entity to its response DTO.
     */
    private LocationAdminDTO toDTO(Location location) {

        LocationAdminDTO dto = new LocationAdminDTO();

        dto.setId(location.getIdLocation());
        dto.setName(location.getLocationName());
        dto.setCountry(location.getCountry());
        dto.setPosition(location.getPosition());

        return dto;
    }
}