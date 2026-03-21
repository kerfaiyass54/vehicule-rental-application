package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.ClientDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ClientService {
    void addClient(Client client, String locationName);
    void deleteClient(long id);
    ClientDTO getClient(long id);
    void updateClient(ClientDTO clientDTO, long id);
    void changePassword(long id, String newPassword);
    void addToBudget(double budgetExtra, long id);
    void changeLocation(long id, String newLocation);
    Page<ClientDTO> listOfClients( int page, int size, String search);
    List<String> getCLientEmails();

}
