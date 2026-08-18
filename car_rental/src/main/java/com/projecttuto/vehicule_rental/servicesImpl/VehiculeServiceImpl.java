package com.projecttuto.vehicule_rental.servicesImpl;



import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeListDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeResultDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeUpdate;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.services.VehiculeService;

import java.util.Optional;

@Service
@Slf4j
public class VehiculeServiceImpl implements VehiculeService {

    private final VehiculeRepository vehiculeRepository;
    private final SupplierRepository supplierRepository;


    public VehiculeServiceImpl(VehiculeRepository vehiculeRepository, SupplierRepository supplierRepository) {
        this.vehiculeRepository = vehiculeRepository;
        this.supplierRepository = supplierRepository;
    }

    public VehiculeDTO getVehicule(Vehicule vehicule){
        VehiculeDTO vehiculeDTO = new VehiculeDTO();
        vehiculeDTO.setIdVehicule(vehicule.getIdVehicule());
        vehiculeDTO.setNameVehicule(vehicule.getNameVehicule());
        vehiculeDTO.setSupplier(vehicule.getSupplier().getEmail());
        vehiculeDTO.setBrand(vehicule.getBrand());
        vehiculeDTO.setPrice(vehicule.getPrice());
        vehiculeDTO.setHighSpeed(vehicule.getHighSpeed());
        vehiculeDTO.setTransmission(vehicule.getTransmission());
        vehiculeDTO.setVehiculeStatus(vehicule.getVehiculeStatus());
        return vehiculeDTO;
    }


    public VehiculeListDTO getVehiculeList(Vehicule vehicule){
        VehiculeListDTO vehiculeListDTO = new VehiculeListDTO();
        vehiculeListDTO.setIdVehicule(vehicule.getIdVehicule());
        vehiculeListDTO.setTransmission(vehicule.getTransmission());
        vehiculeListDTO.setPrice(vehicule.getPrice());
        vehiculeListDTO.setNameVehicule(vehicule.getNameVehicule());
        return vehiculeListDTO;
    }

    @Override
    public VehiculeDTO addVehicule(VehiculeDTO vehiculeDTO){
        Vehicule vehicule = new Vehicule();
        vehicule.setNameVehicule(vehiculeDTO.getNameVehicule());
        vehicule.setColor(vehiculeDTO.getColor());
        vehicule.setBrand(vehiculeDTO.getBrand());
        vehicule.setPrice(vehiculeDTO.getPrice());
        vehicule.setHighSpeed(vehiculeDTO.getHighSpeed());
        vehicule.setTransmission(vehiculeDTO.getTransmission());
        vehicule.setVehiculeStatus(vehiculeDTO.getVehiculeStatus());
        vehicule.setSupplier(supplierRepository.findSupplierByEmail(vehiculeDTO.getSupplier()));
        vehiculeRepository.save(vehicule);
        return getVehicule(vehicule);
    }

    @Override
    public Page<VehiculeListDTO> getVehiculeList(int size, int page, String supplierName){
        Pageable pageable = PageRequest.of(page, size);
        Supplier supplier = supplierRepository.findSupplierBySuppName(supplierName);
        return vehiculeRepository.findVehiculesBySupplier(supplier, pageable).map(this::getVehiculeList);
    }

    @Override
    public VehiculeDTO getVehiculeById(Long id){
        Optional<Vehicule> vehicule =  vehiculeRepository.findById(id);
        return vehicule.map(this::getVehicule).orElse(null);
    }

    @Override
    public void updateVehicule(VehiculeUpdate vehiculeUpdate, Long id){
        Optional<Vehicule> vehicule =  vehiculeRepository.findById(id);
        if(vehicule.isPresent()){
            Vehicule vehiculeUpdated = vehicule.get();
            vehiculeUpdated.setColor(vehiculeUpdate.getColor());
            vehiculeUpdated.setHighSpeed(vehiculeUpdate.getHighSpeed());
            vehiculeUpdated.setPrice(vehiculeUpdate.getPrice());
        }
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

        return vehicules.map(VehiculeMapper::toDTO);
    }


}
