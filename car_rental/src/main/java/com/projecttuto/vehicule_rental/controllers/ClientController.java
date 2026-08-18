package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.dto.ClientDashboardDTO;
import com.projecttuto.vehicule_rental.dto.ClientUpdateDTO;
import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.dto.OpenTicketDTO;
import com.projecttuto.vehicule_rental.dto.OwnedVehiculeDTO;
import com.projecttuto.vehicule_rental.dto.TicketInfoDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projecttuto.vehicule_rental.services.ClientService;
import org.springframework.data.domain.Page;

import java.util.List;


@RestController
@RequestMapping("/client")
@CrossOrigin("*")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }



    @PostMapping("/")
    public ResponseEntity<Void> addClient(@RequestBody Client client, @RequestParam String locationName){
        clientService.addClient(client,locationName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{email}")
    public ResponseEntity<Client> updateClient(
            @PathVariable String email,
            @RequestBody ClientUpdateDTO clientUpdateDTO) {

        Client updatedClient = clientService.updateClient(email, clientUpdateDTO);

        return ResponseEntity.ok(updatedClient);
    }
    @PutMapping("/location/{email}")
    public ResponseEntity<LocationDTO> updateClientLocation(
            @PathVariable String email,
            @RequestBody LocationDTO locationDTO) {

        return ResponseEntity.ok(
                clientService.updateClientLocation(email, locationDTO));
    }

    @GetMapping("/dashboard/{email}")
    public ResponseEntity<ClientDashboardDTO> getDashboard(
            @PathVariable String email) {

        return ResponseEntity.ok(
                clientService.getDashboard(email));
    }


    @GetMapping("/{email}/tickets")
    public ResponseEntity<Page<TicketInfoDTO>> getClientTickets(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                clientService.getClientTickets(email, page, size));
    }

    @GetMapping("/{email}/owned-vehicules")
    public ResponseEntity<Page<OwnedVehiculeDTO>> getOwnedVehicules(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                clientService.getOwnedVehicules(email, page, size));
    }


    @PostMapping("/ticket")
    public ResponseEntity<TicketInfoDTO> openTicket(
            @RequestBody OpenTicketDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.openTicket(dto));
    }


    
    @GetMapping("/emails")
    public ResponseEntity<List<String>> getClientEmails(){
        List<String> emails = clientService.getCLientEmails();
        if (emails != null) {
            return ResponseEntity.ok(emails);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
