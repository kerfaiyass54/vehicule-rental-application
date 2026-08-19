package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.AddressDTO;
import com.projecttuto.vehicule_rental.dto.AddressSupplierDTO;
import com.projecttuto.vehicule_rental.services.SupplierAddressService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
public class SupplierAddressController {

    private final SupplierAddressService supplierAddressService;

    @GetMapping("/suppliers/{email}/addresses/count")
    public ResponseEntity<Integer> getSupplierAddresses(
            @PathVariable String email) {

        log.info(
                "Fetching address count for supplier: {}",
                email
        );

        return ResponseEntity.ok(
                supplierAddressService.getSupplierAddresses(email)
        );
    }

    @PostMapping("/supplier-addresses")
    public ResponseEntity<AddressDTO> addAddressToSupplier(
            @Valid @RequestBody AddressDTO addressDTO) {

        log.info(
                "Adding address for supplier: {}",
                addressDTO.getSupplierEmail()
        );

        AddressDTO createdAddress =
                supplierAddressService.addAddressToSupplier(addressDTO);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdAddress);
    }

    @GetMapping("/suppliers/{email}/addresses")
    public ResponseEntity<Page<AddressSupplierDTO>> getSuppliersAddresses(
            @PathVariable String email,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info(
                "Fetching addresses for supplier: {}, page: {}, size: {}",
                email,
                page,
                size
        );

        Page<AddressSupplierDTO> addresses =
                supplierAddressService.getSuppliersAddresses(
                        page,
                        size,
                        email
                );

        return ResponseEntity.ok(addresses);
    }

    @PatchMapping("/supplier-addresses/{id}/free")
    public ResponseEntity<Void> freeAddress(
            @PathVariable Long id) {

        log.info("Freeing address with id: {}", id);

        supplierAddressService.freeAddress(id);

        return ResponseEntity.noContent().build();
    }
}