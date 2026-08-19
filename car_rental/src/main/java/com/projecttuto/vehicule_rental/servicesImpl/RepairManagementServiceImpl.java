package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.RepairAdminDTO;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.exception.ResourceNotFoundException;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.services.RepairManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepairManagementServiceImpl implements RepairManagementService {

    private final RepairRepository repairRepository;
    private final LocationRepository locationRepository;


    @Override
    public Page<RepairAdminDTO> getRepairs(int page, int size) {

        Pageable pageable = createPageable(page, size);

        return repairRepository.findAll(pageable)
                .map(this::mapToDTO);
    }


    @Override
    public RepairAdminDTO getRepair(Long id) {

        Repair repair = findRepairById(id);

        return mapToDTO(repair);
    }


    @Override
    public RepairAdminDTO updateRepair(
            Long id,
            RepairAdminDTO dto) {

        Repair repair = findRepairById(id);

        updateRepairFields(repair, dto);

        Repair savedRepair = repairRepository.save(repair);

        return mapToDTO(savedRepair);
    }


    @Override
    public void deleteRepair(Long id) {

        Repair repair = findRepairById(id);

        repairRepository.delete(repair);
    }


    // =========================
    // PRIVATE METHODS
    // =========================

    private Repair findRepairById(Long id) {

        return repairRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Repair center not found"
                        )
                );
    }


    private Location findLocationById(Long locationId) {

        return locationRepository.findById(locationId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Location not found"
                        )
                );
    }


    private void updateRepairFields(
            Repair repair,
            RepairAdminDTO dto) {

        repair.setRepairName(dto.getNameRepair());

        updateLocationIfProvided(repair, dto.getLocationId());
    }


    private void updateLocationIfProvided(
            Repair repair,
            Long locationId) {

        if (locationId != null) {

            Location location = findLocationById(locationId);

            repair.setLocation(location);
        }
    }


    private RepairAdminDTO mapToDTO(Repair repair) {

        RepairAdminDTO dto = new RepairAdminDTO();

        dto.setId(repair.getIdRepair());
        dto.setNameRepair(repair.getRepairName());
        dto.setEmail(repair.getEmail());

        mapLocation(repair, dto);

        return dto;
    }


    private void mapLocation(
            Repair repair,
            RepairAdminDTO dto) {

        if (repair.getLocation() != null) {

            dto.setLocationId(
                    repair.getLocation().getIdLocation()
            );

            dto.setLocationName(
                    repair.getLocation().getLocationName()
            );
        }
    }


    private Pageable createPageable(
            int page,
            int size) {

        return PageRequest.of(page, size);
    }
}