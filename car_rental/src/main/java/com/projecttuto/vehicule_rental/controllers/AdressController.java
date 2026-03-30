package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.AdressDTO;
import com.projecttuto.vehicule_rental.DTO.AdressSupplierDTO;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projecttuto.vehicule_rental.services.AdressService;

import java.util.List;

@RestController
@RequestMapping("/adress")
@CrossOrigin("*")
public class AdressController {


    private final AdressService adressService;

    public AdressController(AdressService adressService) {
        this.adressService = adressService;
    }

    @PostMapping("/")
    public ResponseEntity<AdressDTO>  addAdress(@RequestBody AdressDTO adressDTO) {
        AdressDTO adressDTO1 = adressService.addAddressToSupplier(adressDTO);
        return ResponseEntity.ok().body(adressDTO1);
    }

    @GetMapping("/")
    public ResponseEntity<Page<AdressSupplierDTO>> getSuppliersAdresses(@RequestParam int page,@RequestParam int size,@RequestParam String supplierName) {
        Page<AdressSupplierDTO> adresses = adressService.getSuppliersAdresses(page, size, supplierName);
        return ResponseEntity.ok().body(adresses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdress(@PathVariable Long id){
        adressService.freeAdress(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/total/{supplierName}")
    public ResponseEntity<Integer> getTotalAdresses(@PathVariable String supplierName) {
        Integer total = adressService.getTotalAdresses(supplierName);
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/location/{locationName}")
    public ResponseEntity<Integer>  getLocationsAdressesNumber(@PathVariable String locationName) {
        Integer numberOfAdresses = adressService.getAdressesPerLocation(locationName);
        return ResponseEntity.ok().body(numberOfAdresses);
    }

    @GetMapping("/locations/{supplierName}")
    public ResponseEntity<List<String>> getLocations(@PathVariable String supplierName) {
        List<String> locations = adressService.getLocations(supplierName);
        return ResponseEntity.ok().body(locations);
    }

    @GetMapping("/countries/{supplierName}")
    public ResponseEntity<List<String>> getCountries(@PathVariable String supplierName) {
        List<String> countries = adressService.getCountries(supplierName);
        return ResponseEntity.ok().body(countries);
    }






}
