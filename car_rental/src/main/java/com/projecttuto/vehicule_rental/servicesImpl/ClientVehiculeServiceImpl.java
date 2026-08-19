package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.OwnedVehiculeDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.repositories.BuyingRepository;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
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


    @Override
    public Page<OwnedVehiculeDTO> getOwnedVehicules(String clientEmail, int page, int size) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return buyingRepository.findByClient(client, pageable)
                .map(buying -> {

                    Vehicule vehicule = buying.getVehicle();

                    OwnedVehiculeDTO dto = new OwnedVehiculeDTO();

                    dto.setNameVehicule(vehicule.getVehicleName());
                    dto.setBrand(vehicule.getBrand());
                    dto.setTransmission(vehicule.getTransmission());

                    if (vehicule.getSupplier() != null) {
                        dto.setSupplierName(
                                vehicule.getSupplier().getSupplierName());
                    }

                    return dto;
                });
    }


}
