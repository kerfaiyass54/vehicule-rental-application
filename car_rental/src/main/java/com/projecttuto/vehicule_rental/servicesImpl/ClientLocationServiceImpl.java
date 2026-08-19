package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.services.ClientLocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class ClientLocationServiceImpl implements ClientLocationService {

    private final ClientRepository clientRepository;

    private final LocationRepository locationRepository;



    @Override
    public LocationDTO updateClientLocation(String clientEmail, LocationDTO locationDTO) {

        Client client = clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException("Client not found");
        }

        Location location = locationRepository.findById(locationDTO.getIdLoc())
                .orElseThrow(() -> new RuntimeException("Location not found"));

        client.setLocation(location);
        clientRepository.save(client);

        return locationDTO;
    }
}
