package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.AddressCreation;
import com.projecttuto.vehicule_rental.dto.AddressDTO;
import com.projecttuto.vehicule_rental.dto.BuyingResponseDTO;
import com.projecttuto.vehicule_rental.dto.DemandResponseDTO;
import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.dto.SubscriptionResponseDTO;
import com.projecttuto.vehicule_rental.dto.SupplierDashboardDTO;
import com.projecttuto.vehicule_rental.dto.SupplierDetailsDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeCreation;
import com.projecttuto.vehicule_rental.dto.VehiculeDTO;
import com.projecttuto.vehicule_rental.entities.Address;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.projecttuto.vehicule_rental.services.SupplierService;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;

import java.util.List;

@RestController
@RequestMapping("/supplier")
@CrossOrigin("*")
public class SupplierController {


    private final SupplierService supplierService;

    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/details/{email}")
    public ResponseEntity<SupplierDetailsDTO> getSupplierDetails(@PathVariable("email") String email){
        SupplierDetailsDTO supplierDetailsDTO = supplierService.getDetails(email);
        return ResponseEntity.ok().body(supplierDetailsDTO);
    }

    @GetMapping("/{email}/vehicules/total")
    public ResponseEntity<Integer> getSupplierVehicules(
            @PathVariable("email") String email) {

        Integer numberOfVehicules = supplierService.getSupplierVehicules(email);
        return ResponseEntity.ok(numberOfVehicules);
    }





    @GetMapping("/{email}/addresses/total")
    public ResponseEntity<Integer> getSupplierAddresses(
            @PathVariable("email") String email) {

        Integer numberOfAddresses = supplierService.getSupplierAddresses(email);
        return ResponseEntity.ok(numberOfAddresses);
    }


    @GetMapping("/{email}/countries/total")
    public ResponseEntity<Integer> getSupplierCountries(
            @PathVariable("email") String email) {

        Integer numberOfCountries = supplierService.getSupplierCountries(email);
        return ResponseEntity.ok(numberOfCountries);
    }


    @GetMapping("/{email}/locations")
    public ResponseEntity<Integer> getSupplierLocations(
            @PathVariable("email") String email) {

        Integer numberOfLocations = supplierService.getSupplierLocations(email);
        return ResponseEntity.ok(numberOfLocations);
    }


    @GetMapping("/{email}/addresses")
    public ResponseEntity<List<AddressDTO>> getAddressesList(
            @PathVariable("email") String email,
            @RequestParam("size") int size,
            @RequestParam("page") int page) {

        List<AddressDTO> addresses = supplierService.getAddressesList(email, size, page);
        return ResponseEntity.ok(addresses);
    }


    @GetMapping("/{email}/countries")
    public ResponseEntity<List<String>> getCountries(
            @PathVariable("email") String email) {

        List<String> countries = supplierService.getCountries(email);
        return ResponseEntity.ok(countries);
    }


    @GetMapping("/{email}/locations")
    public ResponseEntity<List<LocationDTO>> getLocations(
            @PathVariable("email") String email,
            @RequestParam("size") int size,
            @RequestParam("page") int page) {

        List<LocationDTO> locations = supplierService.getLocations(email, size, page);
        return ResponseEntity.ok(locations);
    }

    @GetMapping("/{email}/subscriptions")
    public ResponseEntity<Page<SubscriptionResponseDTO>> checkSubscriptions(

            @PathVariable String email,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                supplierService.checkSubscriptions(
                        email,
                        page,
                        size));
    }

    @GetMapping("/{email}/buyings")
    public ResponseEntity<Page<BuyingResponseDTO>> checkBuyings(

            @PathVariable String email,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                supplierService.checkBuyings(
                        email,
                        page,
                        size));
    }

    @GetMapping("/{email}/demands")
    public ResponseEntity<Page<DemandResponseDTO>> checkDemands(

            @PathVariable String email,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        return ResponseEntity.ok(
                supplierService.checkDemands(
                        email,
                        page,
                        size));
    }

    @GetMapping("/{email}/dashboard")
    public ResponseEntity<SupplierDashboardDTO> getDashboard(
            @PathVariable String email) {

        return ResponseEntity.ok(
                supplierService.getDashboard(email));
    }

    @PutMapping("/{demandId}/demands/approve")
    public ResponseEntity<DemandResponseDTO> approveDemand(
            @PathVariable Long demandId) {

        return ResponseEntity.ok(
                supplierService.approveDemand(demandId));
    }

    @PutMapping("/{demandId}/demands/refuse")
    public ResponseEntity<DemandResponseDTO> refuseDemand(
            @PathVariable Long demandId) {

        return ResponseEntity.ok(
                supplierService.refuseDemand(demandId));
    }



    @GetMapping("/{email}/vehicules/status/{status}")
    public ResponseEntity<Integer> countVehiculesByStatus(
            @PathVariable("email") String email,
            @PathVariable("status") VehiculeStatus status) {

        Integer count = supplierService
                .countBySupplierEmailAndVehiculeStatus(email, status);

        return ResponseEntity.ok(count);
    }


    @GetMapping("/{email}/vehicules")
    public ResponseEntity<List<VehiculeDTO>> getVehiculesList(
            @PathVariable("email") String email) {

        List<VehiculeDTO> vehicules = supplierService.getVehiculesList(email);
        return ResponseEntity.ok(vehicules);
    }





    @PostMapping("/{email}/vehicules")
    public ResponseEntity<Vehicule> addVehiculeNew(
            @RequestBody VehiculeCreation vehiculeCreation,
            @PathVariable("email") String email) {

        Vehicule vehicule = supplierService.addVehiculeNew(vehiculeCreation, email);
        return ResponseEntity.ok(vehicule);
    }


    @PostMapping("/{email}/addresses")
    public ResponseEntity<Address> addAddressNew(
            @RequestBody AddressCreation addressCreation,
            @PathVariable("email") String email) {

        Address adress = supplierService.addAddressNew(addressCreation, email);
        return ResponseEntity.ok(adress);
    }


    @GetMapping("/{email}/vehicules/names")
    public ResponseEntity<List<String>> getVehiculesNames(
            @PathVariable("email") String email) {

        List<String> names = supplierService.getVehiculesNames(email);
        return ResponseEntity.ok(names);
    }



    @PatchMapping("/{addressId}")
    public ResponseEntity<String> freeAddress(
            @PathVariable("addressId") Long addressId) {

        supplierService.freeAddress(addressId);
        return ResponseEntity.ok("Address freed successfully");
    }

    @GetMapping("/{email}/vehicules/ids")
    public ResponseEntity<List<Long>> getVehicules(@PathVariable String email) {
        List<Long> vehicules = supplierService.getVehiculesIds(email);
        return ResponseEntity.ok(vehicules);
    }






}
