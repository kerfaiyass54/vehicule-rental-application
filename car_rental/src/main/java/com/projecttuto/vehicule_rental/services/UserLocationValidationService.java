package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.LocationValidationDTO;

import java.util.List;

public interface UserLocationValidationService {

    // =========================================================
    // GET ALL USER NAMES
    // =========================================================

    List<String> getAllNames();


    // =========================================================
    // GET ALL EMAILS
    // =========================================================

    List<String> getAllEmails();


    // =========================================================
    // GET ALL LOCATIONS
    // =========================================================

    List<LocationValidationDTO> getAllLocations();


    // =========================================================
    // CHECK NAME
    // =========================================================

    boolean nameExists(String name);


    // =========================================================
    // CHECK EMAIL
    // =========================================================

    boolean emailExists(String email);


    // =========================================================
    // CHECK LOCATION
    // =========================================================

    boolean locationExists(
            String name,
            String country
    );
}