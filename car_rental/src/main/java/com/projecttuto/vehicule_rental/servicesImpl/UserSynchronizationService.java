package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizationService {

    private final KeycloakAdminServiceImpl keycloakAdminService;

    private final ClientRepository clientRepository;

    private final SupplierRepository supplierRepository;

    private final RepairRepository repairRepository;


    // =========================================================
    // STARTUP
    // =========================================================

    @EventListener(ApplicationReadyEvent.class)
    public void synchronize() {

        log.info("==============================================");
        log.info("STARTING USER SYNCHRONIZATION");
        log.info("==============================================");

        try {

            /*
             * First:
             *
             * Keycloak -> Database
             */
            synchronizeKeycloakToDatabase();

            /*
             * Then:
             *
             * Database -> Keycloak
             */
            synchronizeDatabaseToKeycloak();

            log.info("==============================================");
            log.info("USER SYNCHRONIZATION COMPLETED");
            log.info("==============================================");

        } catch (Exception e) {

            log.error(
                    "USER SYNCHRONIZATION FAILED",
                    e
            );
        }
    }


    // =========================================================
    // KEYCLOAK -> DATABASE
    // =========================================================

    private void synchronizeKeycloakToDatabase() {

        log.info(
                "Synchronizing Keycloak -> Database"
        );

        List<UserRepresentation> users =
                keycloakAdminService.getAllUsers();

        if (users == null || users.isEmpty()) {

            log.info(
                    "No Keycloak users found."
            );

            return;
        }

        for (UserRepresentation user : users) {

            try {

                synchronizeKeycloakUser(user);

            } catch (Exception e) {

                log.error(
                        "Unable to synchronize Keycloak user {}",
                        user.getEmail(),
                        e
                );
            }
        }
    }


    private void synchronizeKeycloakUser(
            UserRepresentation user) {

        String email = user.getEmail();

        if (email == null ||
                email.isBlank()) {

            log.warn(
                    "Skipping Keycloak user {}: email missing",
                    user.getId()
            );

            return;
        }

        String role =
                keycloakAdminService
                        .getPrimaryRole(user.getId());

        if (role == null) {

            log.warn(
                    "Skipping {}: application role not found",
                    email
            );

            return;
        }

        switch (role.toUpperCase()) {

            case "CLIENT" ->
                    synchronizeClient(user);

            case "SUPPLIER" ->
                    synchronizeSupplier(user);

            case "REPAIR" ->
                    synchronizeRepair(user);

            case "ADMIN" ->
                    synchronizeAdmin(user);

            default ->
                    log.warn(
                            "Unknown role {} for {}",
                            role,
                            email
                    );
        }
    }


    // =========================================================
    // CLIENT
    // =========================================================

    private void synchronizeClient(
            UserRepresentation user) {

        Client client =
                clientRepository
                        .findClientByEmail(
                                user.getEmail()
                        );

        if (client == null) {

            client = new Client();

            client.setEmail(
                    user.getEmail()
            );

            client.setClientName(
                    user.getUsername()
            );

            clientRepository.save(client);

            log.info(
                    "Created Client from Keycloak: {}",
                    user.getEmail()
            );
        }
    }


    // =========================================================
    // SUPPLIER
    // =========================================================

    private void synchronizeSupplier(
            UserRepresentation user) {

        Supplier supplier =
                supplierRepository
                        .findSupplierByEmail(
                                user.getEmail()
                        );

        if (supplier == null) {

            supplier = new Supplier();

            supplier.setEmail(
                    user.getEmail()
            );

            supplier.setSupplierName(
                    user.getUsername()
            );

            supplierRepository.save(supplier);

            log.info(
                    "Created Supplier from Keycloak: {}",
                    user.getEmail()
            );
        }
    }


    // =========================================================
    // REPAIR
    // =========================================================

    private void synchronizeRepair(
            UserRepresentation user) {

        Repair repair =
                repairRepository
                        .findRepairByEmail(
                                user.getEmail()
                        );

        if (repair == null) {

            repair = new Repair();

            repair.setEmail(
                    user.getEmail()
            );

            repair.setRepairName(
                    user.getUsername()
            );

            repairRepository.save(repair);

            log.info(
                    "Created Repair from Keycloak: {}",
                    user.getEmail()
            );
        }
    }


    // =========================================================
    // ADMIN
    // =========================================================

    private void synchronizeAdmin(
            UserRepresentation user) {

        /*
         * You haven't provided AdminRepository/Admin entity
         * yet, so this part intentionally does not guess
         * their fields.
         *
         * Once you provide Admin.java and AdminRepository.java,
         * this becomes exactly the same as Client/Supplier/Repair.
         */

        log.info(
                "ADMIN found in Keycloak: {}",
                user.getEmail()
        );
    }


    // =========================================================
    // DATABASE -> KEYCLOAK
    // =========================================================

    private void synchronizeDatabaseToKeycloak() {

        log.info(
                "Synchronizing Database -> Keycloak"
        );

        synchronizeClients();

        synchronizeSuppliers();

        synchronizeRepairs();

        synchronizeAdmins();
    }


    // =========================================================
    // CLIENTS -> KEYCLOAK
    // =========================================================

    private void synchronizeClients() {

        List<Client> clients =
                clientRepository.findAll();

        for (Client client : clients) {

            if (client.getEmail() == null ||
                    client.getEmail().isBlank()) {

                continue;
            }

            keycloakAdminService.synchronizeUser(
                    client.getEmail(),
                    client.getClientName(),
                    "CLIENT"
            );
        }
    }


    // =========================================================
    // SUPPLIERS -> KEYCLOAK
    // =========================================================

    private void synchronizeSuppliers() {

        List<Supplier> suppliers =
                supplierRepository.findAll();

        for (Supplier supplier : suppliers) {

            if (supplier.getEmail() == null ||
                    supplier.getEmail().isBlank()) {

                continue;
            }

            keycloakAdminService.synchronizeUser(
                    supplier.getEmail(),
                    supplier.getSupplierName(),
                    "SUPPLIER"
            );
        }
    }


    // =========================================================
    // REPAIRS -> KEYCLOAK
    // =========================================================

    private void synchronizeRepairs() {

        List<Repair> repairs =
                repairRepository.findAll();

        for (Repair repair : repairs) {

            if (repair.getEmail() == null ||
                    repair.getEmail().isBlank()) {

                continue;
            }

            keycloakAdminService.synchronizeUser(
                    repair.getEmail(),
                    repair.getRepairName(),
                    "REPAIR"
            );
        }
    }


    // =========================================================
    // ADMINS -> KEYCLOAK
    // =========================================================

    private void synchronizeAdmins() {

        /*
         * Add AdminRepository here when you provide
         * your Admin entity/repository.
         */
    }
}