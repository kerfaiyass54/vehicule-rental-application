package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.LocationValidationDTO;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.services.UserLocationValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserLocationValidationServiceImpl
        implements UserLocationValidationService {


    private final AdminRepository adminRepository;

    private final ClientRepository clientRepository;

    private final SupplierRepository supplierRepository;

    private final RepairRepository repairRepository;

    private final LocationRepository locationRepository;


    // =========================================================
    // GET ALL USER NAMES
    // =========================================================

    @Override
    public List<String> getAllNames() {

        List<String> names =
                new ArrayList<>();


        // -----------------------------------------------------
        // ADMINS
        // -----------------------------------------------------

        adminRepository
                .findAll()
                .forEach(admin ->
                        names.add(
                                admin.getAdminName()
                        )
                );


        // -----------------------------------------------------
        // CLIENTS
        // -----------------------------------------------------

        clientRepository
                .findAll()
                .forEach(client ->
                        names.add(
                                client.getClientName()
                        )
                );


        // -----------------------------------------------------
        // SUPPLIERS
        // -----------------------------------------------------

        supplierRepository
                .findAll()
                .forEach(supplier ->
                        names.add(
                                supplier.getSupplierName()
                        )
                );


        // -----------------------------------------------------
        // REPAIRS
        // -----------------------------------------------------

        repairRepository
                .findAll()
                .forEach(repair ->
                        names.add(
                                repair.getRepairName()
                        )
                );


        return names
                .stream()
                .filter(name ->
                        name != null &&
                                !name.isBlank()
                )
                .distinct()
                .toList();

    }


    // =========================================================
    // GET ALL EMAILS
    // =========================================================

    @Override
    public List<String> getAllEmails() {

        List<String> emails =
                new ArrayList<>();


        // -----------------------------------------------------
        // ADMINS
        // -----------------------------------------------------

        adminRepository
                .findAll()
                .forEach(admin ->
                        emails.add(
                                admin.getEmail()
                        )
                );


        // -----------------------------------------------------
        // CLIENTS
        // -----------------------------------------------------

        clientRepository
                .findAll()
                .forEach(client ->
                        emails.add(
                                client.getEmail()
                        )
                );


        // -----------------------------------------------------
        // SUPPLIERS
        // -----------------------------------------------------

        supplierRepository
                .findAll()
                .forEach(supplier ->
                        emails.add(
                                supplier.getEmail()
                        )
                );


        // -----------------------------------------------------
        // REPAIRS
        // -----------------------------------------------------

        repairRepository
                .findAll()
                .forEach(repair ->
                        emails.add(
                                repair.getEmail()
                        )
                );


        return emails
                .stream()
                .filter(email ->
                        email != null &&
                                !email.isBlank()
                )
                .distinct()
                .toList();

    }


    // =========================================================
    // GET ALL LOCATIONS
    // =========================================================

    @Override
    public List<LocationValidationDTO> getAllLocations() {

        return locationRepository
                .findAll()
                .stream()

                .map(location ->
                        new LocationValidationDTO(
                                location.getLocationName(),
                                location.getCountry()
                        )
                )

                .toList();

    }


    // =========================================================
    // CHECK NAME
    // =========================================================

    @Override
    public boolean nameExists(
            String name
    ) {

        if (
                name == null ||
                        name.isBlank()
        ) {

            return false;

        }


        String normalizedName =
                name.trim();


        return getAllNames()
                .stream()
                .anyMatch(existingName ->
                        existingName.equalsIgnoreCase(
                                normalizedName
                        )
                );

    }


    // =========================================================
    // CHECK EMAIL
    // =========================================================

    @Override
    public boolean emailExists(
            String email
    ) {

        if (
                email == null ||
                        email.isBlank()
        ) {

            return false;

        }


        String normalizedEmail =
                email.trim();


        return getAllEmails()
                .stream()
                .anyMatch(existingEmail ->
                        existingEmail.equalsIgnoreCase(
                                normalizedEmail
                        )
                );

    }


    // =========================================================
    // CHECK LOCATION
    // =========================================================

    @Override
    public boolean locationExists(
            String name,
            String country
    ) {

        if (
                name == null ||
                        name.isBlank() ||
                        country == null ||
                        country.isBlank()
        ) {

            return false;

        }


        String normalizedName =
                name.trim();

        String normalizedCountry =
                country.trim();


        return getAllLocations()
                .stream()
                .anyMatch(location ->

                        location.getName()
                                .equalsIgnoreCase(
                                        normalizedName
                                )

                                &&

                                location.getCountry()
                                        .equalsIgnoreCase(
                                                normalizedCountry
                                        )

                );

    }

}