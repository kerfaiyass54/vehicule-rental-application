package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.AddressDTO;
import com.projecttuto.vehicule_rental.dto.AddressSupplierDTO;
import com.projecttuto.vehicule_rental.services.SupplierAddressService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Slf4j
@Validated
public class SupplierAddressController {

    private final SupplierAddressService supplierAddressService;

    @Operation(
            summary = "Get supplier address count",
            description = "Returns the number of addresses associated with a supplier."
    )
    @GetMapping("/suppliers/{email}/addresses/count")
    public ResponseEntity<Integer> getSupplierAddresses(
            @PathVariable
            @NotBlank(message = "Supplier email is required")
            @Email(message = "Supplier email must be valid")
            String email) {

        log.info("Fetching address count for supplier: {}", email);

        return ResponseEntity.ok(
                supplierAddressService.getSupplierAddresses(email)
        );
    }

    @Operation(
            summary = "Add an address to a supplier",
            description = "Creates and assigns a new address to a supplier."
    )
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

    @Operation(
            summary = "Get supplier addresses",
            description = "Returns a paginated list of addresses belonging to a supplier."
    )
    @GetMapping("/suppliers/{email}/addresses")
    public ResponseEntity<Page<AddressSupplierDTO>> getSuppliersAddresses(
            @PathVariable
            @NotBlank(message = "Supplier email is required")
            @Email(message = "Supplier email must be valid")
            String email,

            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page must be greater than or equal to 0")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Size must be greater than 0")
            int size) {

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

    @Operation(
            summary = "Free a supplier address",
            description = "Marks an address as empty and removes its supplier association."
    )
    @PatchMapping("/supplier-addresses/{id}/free")
    public ResponseEntity<Void> freeAddress(
            @PathVariable
            @Min(value = 1, message = "Address ID must be greater than 0")
            Long id) {

        log.info("Freeing address with id: {}", id);

        supplierAddressService.freeAddress(id);

        return ResponseEntity.noContent().build();
    }
}