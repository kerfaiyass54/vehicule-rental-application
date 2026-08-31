package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.LocationValidationDTO;
import com.projecttuto.vehicule_rental.services.UserLocationValidationService;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/v1/user-location-validation")
public class UserLocationValidationController {


    private final UserLocationValidationService validationService;


    // =========================================================
    // USER NAMES
    // =========================================================

    /**
     * Returns all user names registered in the system.
     *
     * The names are collected from:
     * - Administrators
     * - Clients
     * - Suppliers
     * - Repair centers
     */
    @GetMapping("/names")
    public ResponseEntity<List<String>> getAllNames() {

        return ResponseEntity.ok(
                validationService.getAllNames()
        );

    }


    // =========================================================
    // USER EMAILS
    // =========================================================

    /**
     * Returns all email addresses currently registered
     * across all user-related entities.
     */
    @GetMapping("/emails")
    public ResponseEntity<List<String>> getAllEmails() {

        return ResponseEntity.ok(
                validationService.getAllEmails()
        );

    }


    // =========================================================
    // LOCATIONS
    // =========================================================

    /**
     * Returns all locations with their corresponding countries.
     */
    @GetMapping("/locations")
    public ResponseEntity<List<LocationValidationDTO>> getAllLocations() {

        return ResponseEntity.ok(
                validationService.getAllLocations()
        );

    }


    // =========================================================
    // CHECK NAME EXISTENCE
    // =========================================================

    /**
     * Checks whether a name already exists in the system.
     *
     * This check is performed across:
     * - Admin
     * - Client
     * - Supplier
     * - Repair
     */
    @GetMapping("/names/exists")
    public ResponseEntity<Boolean> nameExists(

            @RequestParam
            @NotBlank(message = "Name must not be blank")
            String name

    ) {

        return ResponseEntity.ok(
                validationService.nameExists(name)
        );

    }


    // =========================================================
    // CHECK EMAIL EXISTENCE
    // =========================================================

    /**
     * Checks whether an email address already exists
     * in any user-related entity.
     */
    @GetMapping("/emails/exists")
    public ResponseEntity<Boolean> emailExists(

            @RequestParam
            @NotBlank(message = "Email must not be blank")
            @Email(message = "Invalid email address")
            String email

    ) {

        return ResponseEntity.ok(
                validationService.emailExists(email)
        );

    }


    // =========================================================
    // CHECK LOCATION EXISTENCE
    // =========================================================

    /**
     * Checks whether a location exists using its
     * name and country.
     */
    @GetMapping("/locations/exists")
    public ResponseEntity<Boolean> locationExists(

            @RequestParam
            @NotBlank(message = "Location name must not be blank")
            String name,

            @RequestParam
            @NotBlank(message = "Country must not be blank")
            String country

    ) {

        return ResponseEntity.ok(
                validationService.locationExists(
                        name,
                        country
                )
        );

    }

}