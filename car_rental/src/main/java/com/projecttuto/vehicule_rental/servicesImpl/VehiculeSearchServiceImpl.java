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

    @Override
    public Page<VehiculeResultDTO> searchVehicules(
            String keyword,
            Transmission transmission,
            VehiculeStatus status,
            Double minPrice,
            Double maxPrice,
            int page,
            int size) {

        Page<Vehicule> vehicules = searchVehicles(
                keyword,
                transmission,
                status,
                minPrice,
                maxPrice,
                page,
                size
        );

        return vehicules.map(this::toDTO);
    }

    /*
     * ============================
     * Vehicle search
     * ============================
     */

    private Page<Vehicule> searchVehicles(
            String keyword,
            Transmission transmission,
            VehiculeStatus status,
            Double minPrice,
            Double maxPrice,
            int page,
            int size) {

        return vehiculeRepository.searchVehicules(
                keyword,
                transmission,
                status,
                minPrice,
                maxPrice,
                PageRequest.of(page, size)
        );
    }

    /*
     * ============================
     * DTO mapping
     * ============================
     */

    private VehiculeResultDTO toDTO(Vehicule vehicule) {

        VehiculeResultDTO dto = new VehiculeResultDTO();

        setVehicleData(dto, vehicule);
        setSupplierData(dto, vehicule);

        return dto;
    }

    private void setVehicleData(
            VehiculeResultDTO dto,
            Vehicule vehicule) {

        dto.setIdVehicule(
                vehicule.getIdVehicle());

        dto.setNameVehicule(
                vehicule.getVehicleName());

        dto.setBrand(
                vehicule.getBrand());

        dto.setColor(
                vehicule.getColor());

        dto.setPrice(
                vehicule.getPrice());

        dto.setHighSpeed(
                vehicule.getMaxSpeed());

        dto.setTransmission(
                vehicule.getTransmission());

        dto.setVehiculeStatus(
                vehicule.getVehicleStatus());
    }

    private void setSupplierData(
            VehiculeResultDTO dto,
            Vehicule vehicule) {

        if (vehicule.getSupplier() != null) {
            dto.setSupplierName(
                    vehicule.getSupplier().getSupplierName());
        }
    }
}