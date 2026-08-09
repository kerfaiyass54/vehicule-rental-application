package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.VehiculeDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeListDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeResultDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeUpdate;
import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


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

    @GetMapping("/results")
    public ResponseEntity<Page<VehiculeResultDTO>> searchVehicules(

            @RequestParam(required = false) String keyword,

            @RequestParam(required = false) Transmission transmission,

            @RequestParam(required = false) VehiculeStatus status,

            @RequestParam(required = false) Double minPrice,

            @RequestParam(required = false) Double maxPrice,

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                vehiculeService.searchVehicules(
                        keyword,
                        transmission,
                        status,
                        minPrice,
                        maxPrice,
                        page,
                        size));
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
