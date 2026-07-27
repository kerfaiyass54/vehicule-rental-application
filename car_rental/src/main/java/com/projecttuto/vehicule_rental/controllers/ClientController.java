package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.*;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projecttuto.vehicule_rental.services.ClientService;
import org.springframework.data.domain.Page;

import java.util.List;


@RestController
@RequestMapping("/client")
@CrossOrigin("*")
public class ClientController {

    @Autowired
    private ClientService clientService;

    private final TicketService ticketService;

    public ClientController(TicketService ticketService) {
        this.ticketService = ticketService;
    }


    @PostMapping("/new")
    public ResponseEntity<Void> addClient(@RequestBody Client client, @RequestParam String locationName){
        clientService.addClient(client,locationName);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<Client> updateClient(
            @PathVariable String email,
            @RequestBody ClientUpdateDTO clientUpdateDTO) {

        Client updatedClient = clientService.updateClient(email, clientUpdateDTO);

        return ResponseEntity.ok(updatedClient);
    }
    @PutMapping("/{email}/location")
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


    @GetMapping("/client/{email}")
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


    @PostMapping("/open")
    public ResponseEntity<TicketInfoDTO> openTicket(
            @RequestBody OpenTicketDTO dto) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(clientService.openTicket(dto));
    }





    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable long id){
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClient(@PathVariable long id){
        ClientDTO client = clientService.getClient(id);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateClient(@RequestBody ClientDTO clientDTO, @PathVariable long id){
        clientService.updateClient(clientDTO , id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/password/{id}")
    public ResponseEntity<Void> changePassword(@PathVariable long id,@RequestParam String newPassword){
        clientService.changePassword(id,newPassword);
        return ResponseEntity.noContent().build();
    }


    @PostMapping("/budget/{id}")
    public ResponseEntity<Void> addToBudget(@RequestParam double budgetExtra,@PathVariable long id){
        clientService.addToBudget(budgetExtra,id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/location/{id}")
    public ResponseEntity<Void> changeLocation(@PathVariable long id,@RequestParam String newLocation){
        clientService.changeLocation(id,newLocation);
        return ResponseEntity.noContent().build();
    }



    @GetMapping("/list/clients")
    public ResponseEntity<Page<ClientDTO>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String search
    ){
        Page<ClientDTO> clientsList= clientService.listOfClients(page, size, search);
        if (clientsList != null) {
            return ResponseEntity.ok(clientsList);
        } else {
            return ResponseEntity.notFound().build();
        }
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
