package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeUpdate;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.exception.SupplierNotFoundException;
import com.projecttuto.vehicule_rental.exception.VehiculeNotFoundException;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.VehiculeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public VehiculeDTO addVehicule(VehiculeDTO vehiculeDTO) {

        Vehicule vehicule = createVehicule(vehiculeDTO);

        setSupplier(vehicule, vehiculeDTO.getSupplier());

        Vehicule saved = vehiculeRepository.save(vehicule);

        return getVehicule(saved);
    }

    @Override
    public VehiculeDTO getVehiculeById(Long id) {

        Vehicule vehicule = findVehiculeById(id);

        return getVehicule(vehicule);
    }

    @Override
    public void updateVehicule(
            VehiculeUpdate vehiculeUpdate,
            Long id) {

        Vehicule vehicule = findVehiculeById(id);

        updateVehicleData(vehicule, vehiculeUpdate);

        vehiculeRepository.save(vehicule);
    }

    /*
     * ============================
     * Vehicle creation
     * ============================
     */

    private Vehicule createVehicule(
            VehiculeDTO dto) {

        Vehicule vehicule = new Vehicule();

        vehicule.setVehicleName(
                dto.getNameVehicule());

        vehicule.setColor(
                dto.getColor());

        vehicule.setBrand(
                dto.getBrand());

        vehicule.setPrice(
                dto.getPrice());

        vehicule.setMaxSpeed(
                dto.getHighSpeed());

        vehicule.setTransmission(
                dto.getTransmission());

        vehicule.setVehicleStatus(
                dto.getVehiculeStatus());

        return vehicule;
    }

    /*
     * ============================
     * Supplier
     * ============================
     */

    private void setSupplier(
            Vehicule vehicule,
            String supplierEmail) {

        var supplier =
                supplierRepository.findSupplierByEmail(
                        supplierEmail);

        if (supplier == null) {
            throw new SupplierNotFoundException(
                    "Supplier not found with email: "
                            + supplierEmail);
        }

        vehicule.setSupplier(supplier);
    }

    /*
     * ============================
     * Vehicle retrieval
     * ============================
     */

    private Vehicule findVehiculeById(Long id) {

        return vehiculeRepository.findById(id)
                .orElseThrow(() ->
                        new VehiculeNotFoundException(
                                "Vehicule not found with id: "
                                        + id));
    }

    /*
     * ============================
     * Vehicle update
     * ============================
     */

    private void updateVehicleData(
            Vehicule vehicule,
            VehiculeUpdate dto) {

        vehicule.setColor(
                dto.getColor());

        vehicule.setMaxSpeed(
                dto.getHighSpeed());

        vehicule.setPrice(
                dto.getPrice());
    }

    /*
     * ============================
     * DTO mapping
     * ============================
     */

    private VehiculeDTO getVehicule(
            Vehicule vehicule) {

        VehiculeDTO dto = new VehiculeDTO();

        dto.setIdVehicule(
                vehicule.getIdVehicle());

        dto.setNameVehicule(
                vehicule.getVehicleName());

        dto.setSupplier(
                vehicule.getSupplier().getEmail());

        dto.setBrand(
                vehicule.getBrand());

        dto.setPrice(
                vehicule.getPrice());

        dto.setHighSpeed(
                vehicule.getMaxSpeed());

        dto.setTransmission(
                vehicule.getTransmission());

        dto.setVehiculeStatus(
                vehicule.getVehicleStatus());

        return dto;
    }
}