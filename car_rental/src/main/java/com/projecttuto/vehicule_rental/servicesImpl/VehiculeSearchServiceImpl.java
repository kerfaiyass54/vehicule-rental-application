package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.VehiculeResultDTO;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.VehiculeSearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehiculeSearchServiceImpl implements VehiculeSearchService {

    private final VehiculeRepository vehiculeRepository;

    private VehiculeResultDTO toDTO(Vehicule vehicule) {

        VehiculeResultDTO dto = new VehiculeResultDTO();

        dto.setIdVehicule(vehicule.getIdVehicle());
        dto.setNameVehicule(vehicule.getVehicleName());
        dto.setBrand(vehicule.getBrand());
        dto.setColor(vehicule.getColor());
        dto.setPrice(vehicule.getPrice());
        dto.setHighSpeed(vehicule.getMaxSpeed());
        dto.setTransmission(vehicule.getTransmission());
        dto.setVehiculeStatus(vehicule.getVehicleStatus());

        if (vehicule.getSupplier() != null) {
            dto.setSupplierName(vehicule.getSupplier().getSupplierName());
        }

        return dto;
    }


    @Override
    public Page<VehiculeResultDTO> searchVehicules(
            String keyword,
            Transmission transmission,
            VehiculeStatus status,
            Double minPrice,
            Double maxPrice,
            int page,
            int size) {

        Page<Vehicule> vehicules = vehiculeRepository.searchVehicules(
                keyword,
                transmission,
                status,
                minPrice,
                maxPrice,
                PageRequest.of(page, size));

        return vehicules.map(this::toDTO);
    }
}
