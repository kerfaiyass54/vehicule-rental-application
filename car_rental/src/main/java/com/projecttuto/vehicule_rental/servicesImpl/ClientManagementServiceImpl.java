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
    public List<String> getCLientEmails(){
        return clientRepository.findAll().stream().map(Client::getEmail).toList();
    }

    @Override
    public Page<ClientAdminDTO> getClients(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return clientRepository.findAll(pageable)
                .map(client -> {

                    ClientAdminDTO dto = new ClientAdminDTO();

                    dto.setId(client.getIdClient());

                    dto.setNameClient(client.getClientName());

                    dto.setEmail(client.getEmail());

                    dto.setNationality(client.getNationality());

                    dto.setBudget(client.getBudget());

                    if(client.getLocation()!=null){
                        dto.setLocationId(client.getLocation().getIdLocation());
                        dto.setLocationName(client.getLocation().getLocationName());
                    }

                    return dto;

                });

    }


    @Override
    public ClientAdminDTO getClient(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        ClientAdminDTO dto = new ClientAdminDTO();

        dto.setId(client.getIdClient());

        dto.setNameClient(client.getClientName());

        dto.setEmail(client.getEmail());

        dto.setNationality(client.getNationality());

        dto.setBudget(client.getBudget());

        if(client.getLocation()!=null){
            dto.setLocationId(client.getLocation().getIdLocation());
            dto.setLocationName(client.getLocation().getLocationName());
        }

        return dto;

    }

    @Override
    public ClientAdminDTO updateClient(Long id, ClientAdminDTO dto) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        client.setClientName(dto.getNameClient());

        client.setNationality(dto.getNationality());

        client.setBudget(dto.getBudget());

        if(dto.getLocationId()!=null){

            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() ->
                            new RuntimeException("Location not found"));

            client.setLocation(location);

        }

        Client saved = clientRepository.save(client);

        ClientAdminDTO response = new ClientAdminDTO();

        response.setId(saved.getIdClient());

        response.setNameClient(saved.getClientName());

        response.setEmail(saved.getEmail());

        response.setNationality(saved.getNationality());

        response.setBudget(saved.getBudget());

        if(saved.getLocation()!=null){
            response.setLocationId(saved.getLocation().getIdLocation());
            response.setLocationName(saved.getLocation().getLocationName());
        }

        return response;

    }

    @Override
    public void deleteClient(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        clientRepository.delete(client);

    }





}
