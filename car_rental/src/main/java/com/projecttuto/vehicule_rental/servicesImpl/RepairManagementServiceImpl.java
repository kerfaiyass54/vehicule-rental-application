package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.RepairAdminDTO;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Repair;
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

        Pageable pageable = PageRequest.of(page, size);

        return repairRepository.findAll(pageable)
                .map(repair -> {

                    RepairAdminDTO dto = new RepairAdminDTO();

                    dto.setId(repair.getIdRepair());

                    dto.setNameRepair(repair.getRepairName());

                    dto.setEmail(repair.getEmail());


                    if (repair.getLocation() != null) {
                        dto.setLocationId(repair.getLocation().getIdLocation());
                        dto.setLocationName(repair.getLocation().getLocationName());
                    }

                    return dto;
                });
    }

    @Override
    public RepairAdminDTO updateRepair(Long id,
                                       RepairAdminDTO dto) {

        Repair repair = repairRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Repair center not found"));

        repair.setRepairName(dto.getNameRepair());

        if (dto.getLocationId() != null) {

            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() ->
                            new RuntimeException("Location not found"));

            repair.setLocation(location);
        }

        Repair saved = repairRepository.save(repair);

        RepairAdminDTO response = new RepairAdminDTO();

        response.setId(saved.getIdRepair());

        response.setNameRepair(saved.getRepairName());

        response.setEmail(saved.getEmail());


        if (saved.getLocation() != null) {
            response.setLocationId(saved.getLocation().getIdLocation());
            response.setLocationName(saved.getLocation().getLocationName());
        }

        return response;
    }

    @Override
    public void deleteRepair(Long id) {

        Repair repair = repairRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Repair center not found"));

        repairRepository.delete(repair);
    }

    @Override
    public RepairAdminDTO getRepair(Long id) {

        Repair repair = repairRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Repair center not found"));

        RepairAdminDTO dto = new RepairAdminDTO();

        dto.setId(repair.getIdRepair());

        dto.setNameRepair(repair.getRepairName());

        dto.setEmail(repair.getEmail());


        if (repair.getLocation() != null) {
            dto.setLocationId(repair.getLocation().getIdLocation());
            dto.setLocationName(repair.getLocation().getLocationName());
        }

        return dto;
    }

}
