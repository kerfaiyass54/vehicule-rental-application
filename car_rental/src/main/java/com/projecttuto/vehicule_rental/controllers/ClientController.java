package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.ClientDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/new")
    public ResponseEntity<Void> addClient(@RequestBody Client client, @RequestParam String locationName){
        clientService.addClient(client,locationName);
        return ResponseEntity.noContent().build();
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
