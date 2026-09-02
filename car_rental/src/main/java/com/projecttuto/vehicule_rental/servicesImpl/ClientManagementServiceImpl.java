package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.ClientAdminDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.LocationRepository;
import com.projecttuto.vehicule_rental.services.ClientManagementService;
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
public class ClientManagementServiceImpl implements ClientManagementService {

    private final ClientRepository clientRepository;
    private final LocationRepository locationRepository;

    @Override
    public ClientAdminDTO createClient(ClientAdminDTO dto) {

        // =========================================================
        // VALIDATION
        // =========================================================

        if (dto == null) {
            throw new IllegalArgumentException(
                    "Client data must not be null."
            );
        }


        // =========================================================
        // NAME UNIQUENESS
        // =========================================================

        if (clientRepository.existsByClientNameIgnoreCase(
                dto.getNameClient().trim()
        )) {

            throw new IllegalArgumentException(
                    "A client with this name already exists."
            );

        }


        // =========================================================
        // EMAIL UNIQUENESS
        // =========================================================

        if (clientRepository.existsByEmailIgnoreCase(
                dto.getEmail().trim()
        )) {

            throw new IllegalArgumentException(
                    "A client with this email already exists."
            );

        }


        // =========================================================
        // LOCATION
        // =========================================================

        Location location =
                locationRepository
                        .findById(dto.getLocationId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "The specified location does not exist."
                                )
                        );


        // =========================================================
        // OPTIONAL LOCATION NAME VALIDATION
        // =========================================================

        if (!location.getLocationName()
                .equalsIgnoreCase(dto.getLocationName().trim())) {

            throw new IllegalArgumentException(
                    "The location name does not match the specified location ID."
            );

        }


        // =========================================================
        // CREATE CLIENT
        // =========================================================

        Client client = new Client();

        client.setClientName(
                dto.getNameClient().trim()
        );

        client.setEmail(
                dto.getEmail().trim()
        );

        client.setNationality(
                dto.getNationality().trim()
        );

        client.setBudget(
                dto.getBudget()
        );

        client.setLocation(
                location
        );


        // =========================================================
        // SAVE
        // =========================================================

        Client savedClient =
                clientRepository.save(client);


        // =========================================================
        // RESPONSE
        // =========================================================

        ClientAdminDTO response =
                new ClientAdminDTO();

        response.setId(
                savedClient.getIdClient()
        );

        response.setNameClient(
                savedClient.getClientName()
        );

        response.setEmail(
                savedClient.getEmail()
        );

        response.setNationality(
                savedClient.getNationality()
        );

        response.setBudget(
                savedClient.getBudget()
        );

        response.setLocationId(
                savedClient.getLocation().getIdLocation()
        );

        response.setLocationName(
                savedClient.getLocation().getLocationName()
        );

        return response;
    }


    @Override
    public List<String> getCLientEmails() {
        return clientRepository.findAll()
                .stream()
                .map(Client::getEmail)
                .toList();
    }

    @Override
    public Page<ClientAdminDTO> getClients(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return clientRepository.findAll(pageable)
                .map(this::mapToDTO);
    }

    @Override
    public ClientAdminDTO getClient(Long id) {

        Client client = findClientById(id);

        return mapToDTO(client);
    }

    @Override
    public ClientAdminDTO updateClient(
            Long id,
            ClientAdminDTO dto) {

        Client client = findClientById(id);

        updateClientFields(client, dto);

        updateLocation(client, dto);

        Client savedClient = saveClient(client);

        return mapToDTO(savedClient);
    }

    @Override
    public void deleteClient(Long id) {

        Client client = findClientById(id);

        deleteClientEntity(client);
    }

    // -------------------------------------------------------------------------
    // Client
    // -------------------------------------------------------------------------

    private Client findClientById(Long id) {

        return clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));
    }

    private Client saveClient(Client client) {

        return clientRepository.save(client);
    }

    private void deleteClientEntity(Client client) {

        clientRepository.delete(client);
    }

    // -------------------------------------------------------------------------
    // Client update
    // -------------------------------------------------------------------------

    private void updateClientFields(
            Client client,
            ClientAdminDTO dto) {

        client.setClientName(dto.getNameClient());
        client.setNationality(dto.getNationality());
        client.setBudget(dto.getBudget());
    }

    private void updateLocation(
            Client client,
            ClientAdminDTO dto) {

        if (dto.getLocationId() == null) {
            return;
        }

        Location location = findLocationById(dto.getLocationId());

        client.setLocation(location);
    }

    // -------------------------------------------------------------------------
    // Location
    // -------------------------------------------------------------------------

    private Location findLocationById(Long locationId) {

        return locationRepository.findById(locationId)
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));
    }

    // -------------------------------------------------------------------------
    // Mapping
    // -------------------------------------------------------------------------

    private ClientAdminDTO mapToDTO(Client client) {

        ClientAdminDTO dto = new ClientAdminDTO();

        dto.setId(client.getIdClient());
        dto.setNameClient(client.getClientName());
        dto.setEmail(client.getEmail());
        dto.setNationality(client.getNationality());
        dto.setBudget(client.getBudget());

        mapLocation(client, dto);

        return dto;
    }

    private void mapLocation(
            Client client,
            ClientAdminDTO dto) {

        if (client.getLocation() == null) {
            return;
        }

        dto.setLocationId(
                client.getLocation().getIdLocation()
        );

        dto.setLocationName(
                client.getLocation().getLocationName()
        );
    }
}