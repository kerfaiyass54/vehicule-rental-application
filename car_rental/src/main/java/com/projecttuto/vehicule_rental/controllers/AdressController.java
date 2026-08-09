package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.AddressDTO;
import com.projecttuto.vehicule_rental.DTO.AddressSupplierDTO;
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
import com.projecttuto.vehicule_rental.services.AddressService;

import java.util.List;

@RestController
@RequestMapping("/adress")
@CrossOrigin("*")
public class AddressController {


    private final AddressService adressService;

    public AddressController(AddressService adressService) {
        this.adressService = adressService;
    }

    @PostMapping("/")
    public ResponseEntity<AddressDTO>  addAddress(@RequestBody AddressDTO adressDTO) {
        AddressDTO adressDTO1 = adressService.addAddressToSupplier(adressDTO);
        return ResponseEntity.ok().body(adressDTO1);
    }

    @GetMapping("/")
    public ResponseEntity<Page<AddressSupplierDTO>> getSuppliersAddresses(@RequestParam int page,@RequestParam int size,@RequestParam String email) {
        Page<AddressSupplierDTO> adresses = adressService.getSuppliersAddresses(page, size, email);
        return ResponseEntity.ok().body(adresses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id){
        adressService.freeAddress(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/adresses/{email}/total")
    public ResponseEntity<Integer> getTotalAddresses(@PathVariable String email) {
        Integer total = adressService.getTotalAddresses(email);
        return ResponseEntity.ok().body(total);
    }

    @GetMapping("/location/{locationName}/adresses/total")
    public ResponseEntity<Integer>  getLocationsAddressesNumber(@PathVariable String locationName) {
        Integer numberOfAddresses = adressService.getAddressesPerLocation(locationName);
        return ResponseEntity.ok().body(numberOfAddresses);
    }

    @GetMapping("/locations/{email}")
    public ResponseEntity<List<String>> getLocations(@PathVariable String email) {
        List<String> locations = adressService.getLocations(email);
        return ResponseEntity.ok().body(locations);
    }

    @GetMapping("/countries/{email}")
    public ResponseEntity<List<String>> getCountries(@PathVariable String email) {
        List<String> countries = adressService.getCountries(email);
        return ResponseEntity.ok().body(countries);
    }






}
