package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.VehiculeDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeListDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeUpdate;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.CategoryName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import com.projecttuto.vehicule_rental.services.VehiculeService;

@RestController
@RequestMapping("/vehicule")
@CrossOrigin("*")
public class VehiculeController {

    private final VehiculeService vehiculeService;

    public VehiculeController(VehiculeService vehiculeService) {
        this.vehiculeService = vehiculeService;
    }

    @PostMapping("/")
    public ResponseEntity<VehiculeDTO> addVehicule(@RequestBody VehiculeDTO vehiculeDTO) {
        VehiculeDTO vehiculeDTO1 = vehiculeService.addVehicule(vehiculeDTO);
        return new ResponseEntity<>(vehiculeDTO1, HttpStatus.CREATED);
    }

    @GetMapping("/")
    public ResponseEntity<Page<VehiculeListDTO>> getVehiculeList(@RequestParam int size, @RequestParam int page, @RequestParam String supplierName){
        Page<VehiculeListDTO> vehiculeListDTOS = vehiculeService.getVehiculeList(size,page,supplierName);
        return new ResponseEntity<>(vehiculeListDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehiculeDTO> getVehicule(@PathVariable Long id){
        VehiculeDTO vehiculeDTO = vehiculeService.getVehiculeById(id);
        return new ResponseEntity<>(vehiculeDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateVehicule(@PathVariable Long id, @RequestBody VehiculeUpdate vehiculeUpdate){
        vehiculeService.updateVehicule(vehiculeUpdate,id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
