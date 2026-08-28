package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.OwnedVehiculeDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeSearchDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.exception.ResourceNotFoundException;
import com.projecttuto.vehicule_rental.exception.VehiculeRentalException;
import com.projecttuto.vehicule_rental.repositories.BuyingRepository;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.ClientVehiculeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClientVehiculeServiceImpl implements ClientVehiculeService {

    private final BuyingRepository buyingRepository;
    private final ClientRepository clientRepository;
    private final VehiculeRepository vehiculeRepository;
    private final SupplierRepository supplierRepository;

    @Override
    public Double getVehiculeTotalPrice(Long vehiculeId, Double reduction){
        Vehicule vehicule = vehiculeRepository.findById(vehiculeId).get();
        return vehicule.getPrice() - reduction;
    }

    private VehiculeSearchDTO mapToSearchDTO(Vehicule vehicule) {

        VehiculeSearchDTO dto = new VehiculeSearchDTO();

        dto.setIdVehicule(vehicule.getIdVehicle());

        dto.setNameVehicule(
                vehicule.getVehicleName()
        );

        dto.setBrand(
                vehicule.getBrand()
        );

        dto.setColor(
                vehicule.getColor()
        );

        dto.setPrice(
                vehicule.getPrice()
        );

        dto.setHighSpeed(
                vehicule.getMaxSpeed()
        );

        dto.setTransmission(
                vehicule.getTransmission()
        );

        dto.setVehiculeStatus(
                vehicule.getVehicleStatus()
        );

        return dto;
    }

    @Override
    public Page<OwnedVehiculeDTO> getOwnedVehicules(
            String clientEmail,
            int page,
            int size) {

        Client client = findClientByEmail(clientEmail);

        Pageable pageable = PageRequest.of(page, size);

        return buyingRepository.findByClient(client, pageable)
                .map(this::toOwnedVehiculeDTO);
    }

    private Client findClientByEmail(String clientEmail) {
        return clientRepository.findClientByEmail(clientEmail)
                != null
                ? clientRepository.findClientByEmail(clientEmail)
                : throwClientNotFound(clientEmail);
    }

    private Client throwClientNotFound(String clientEmail) {
        log.warn("Client not found with email: {}", clientEmail);

        throw new ResourceNotFoundException(
                "Client not found with email: " + clientEmail
        );
    }

    @Override
    public Page<VehiculeSearchDTO> getSupplierVehicules(
            Long supplierId,
            int size,
            int page) {

        Supplier supplier =
                supplierRepository
                        .findByIdSupplier(supplierId);

        if (supplier == null) {
            throw new VehiculeRentalException(
                    "Supplier not found with id: " + supplierId
            );
        }

        Pageable pageable =
                PageRequest.of(page, size);

        Page<Vehicule> vehicules =
                vehiculeRepository.findBySupplier(
                        supplier,
                        pageable
                );

        return vehicules.map(this::mapToSearchDTO);
    }

    private OwnedVehiculeDTO toOwnedVehiculeDTO(
            com.projecttuto.vehicule_rental.entities.Buying buying) {

        Vehicule vehicule = buying.getVehicle();

        OwnedVehiculeDTO dto = new OwnedVehiculeDTO();

        dto.setNameVehicule(vehicule.getVehicleName());
        dto.setBrand(vehicule.getBrand());
        dto.setTransmission(vehicule.getTransmission());

        setSupplierName(dto, vehicule);

        return dto;
    }

    private void setSupplierName(
            OwnedVehiculeDTO dto,
            Vehicule vehicule) {

        if (vehicule.getSupplier() != null) {
            dto.setSupplierName(
                    vehicule.getSupplier().getSupplierName()
            );
        }
    }
}