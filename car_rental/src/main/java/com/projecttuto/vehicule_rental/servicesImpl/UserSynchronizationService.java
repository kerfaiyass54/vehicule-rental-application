package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.entities.Admin;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.repositories.AdminRepository;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserSynchronizationService
        implements CommandLineRunner {


    private final ClientRepository clientRepository;

    private final AdminRepository adminRepository;

    private final SupplierRepository supplierRepository;

    private final RepairRepository repairRepository;

    private final KeycloakAdminServiceImpl keycloakAdminService;


    // =========================================================
    // STARTUP
    // =========================================================

    @Override
    public void run(String... args) {

        log.info("==============================================");
        log.info("STARTING DATABASE -> KEYCLOAK SYNCHRONIZATION");
        log.info("==============================================");


        synchronizeClients();

        synchronizeSuppliers();

        synchronizeAdmins();

        synchronizeRepairs();


        log.info("==============================================");
        log.info("DATABASE -> KEYCLOAK SYNCHRONIZATION COMPLETED");
        log.info("==============================================");
    }


    // =========================================================
    // CLIENTS
    // =========================================================

    private void synchronizeClients() {

        log.info("Synchronizing clients...");

        List<Client> clients =
                clientRepository.findAll();


        for (Client client : clients) {

            try {

                if (client.getEmail() == null ||
                        client.getEmail().isBlank()) {

                    log.warn(
                            "Skipping client without email"
                    );

                    continue;
                }


                String name =
                        client.getClientName();


                if (name == null ||
                        name.isBlank()) {

                    log.warn(
                            "Skipping client without name: {}",
                            client.getEmail()
                    );

                    continue;
                }


                String username =
                        name.toLowerCase()
                                .replaceAll(
                                        "[^a-z0-9._-]",
                                        "_"
                                );


                keycloakAdminService.syncUserToKeycloak(

                        username,

                        name,

                        name,

                        client.getEmail(),

                        "123456",

                        "client"
                );


            } catch (Exception e) {

                log.error(
                        "Failed to synchronize client: {}",
                        client.getEmail(),
                        e
                );
            }
        }
    }


    // =========================================================
    // SUPPLIERS
    // =========================================================

    private void synchronizeSuppliers() {

        log.info("Synchronizing suppliers...");

        List<Supplier> suppliers =
                supplierRepository.findAll();


        for (Supplier supplier : suppliers) {

            try {

                if (supplier.getEmail() == null ||
                        supplier.getEmail().isBlank()) {

                    log.warn(
                            "Skipping supplier without email"
                    );

                    continue;
                }


                String name =
                        supplier.getSupplierName();


                if (name == null ||
                        name.isBlank()) {

                    log.warn(
                            "Skipping supplier without name: {}",
                            supplier.getEmail()
                    );

                    continue;
                }


                String username =
                        name.toLowerCase()
                                .replaceAll(
                                        "[^a-z0-9._-]",
                                        "_"
                                );


                keycloakAdminService.syncUserToKeycloak(

                        username,

                        name,

                        name,

                        supplier.getEmail(),

                        "123456",

                        "supplier"
                );


            } catch (Exception e) {

                log.error(
                        "Failed to synchronize supplier: {}",
                        supplier.getEmail(),
                        e
                );
            }
        }
    }


    // =========================================================
    // ADMINS
    // =========================================================

    private void synchronizeAdmins() {

        log.info("Synchronizing admins...");

        List<Admin> admins =
                adminRepository.findAll();


        for (Admin admin : admins) {

            try {

                if (admin.getEmail() == null ||
                        admin.getEmail().isBlank()) {

                    log.warn(
                            "Skipping admin without email"
                    );

                    continue;
                }


                String name =
                        admin.getAdminName();


                if (name == null ||
                        name.isBlank()) {

                    log.warn(
                            "Skipping admin without name: {}",
                            admin.getEmail()
                    );

                    continue;
                }


                String username =
                        name.toLowerCase()
                                .replaceAll(
                                        "[^a-z0-9._-]",
                                        "_"
                                );


                keycloakAdminService.syncUserToKeycloak(

                        username,

                        name,

                        name,

                        admin.getEmail(),

                        "123456",

                        "admin"
                );


            } catch (Exception e) {

                log.error(
                        "Failed to synchronize admin: {}",
                        admin.getEmail(),
                        e
                );
            }
        }
    }


    // =========================================================
    // REPAIRS
    // =========================================================

    private void synchronizeRepairs() {

        log.info("Synchronizing repairs...");

        List<Repair> repairs =
                repairRepository.findAll();


        for (Repair repair : repairs) {

            try {

                if (repair.getEmail() == null ||
                        repair.getEmail().isBlank()) {

                    log.warn(
                            "Skipping repair without email"
                    );

                    continue;
                }


                String name =
                        repair.getRepairName();


                if (name == null ||
                        name.isBlank()) {

                    log.warn(
                            "Skipping repair without name: {}",
                            repair.getEmail()
                    );

                    continue;
                }


                String username =
                        name.toLowerCase()
                                .replaceAll(
                                        "[^a-z0-9._-]",
                                        "_"
                                );


                keycloakAdminService.syncUserToKeycloak(

                        username,

                        name,

                        name,

                        repair.getEmail(),

                        "123456",

                        "repair"
                );


            } catch (Exception e) {

                log.error(
                        "Failed to synchronize repair: {}",
                        repair.getEmail(),
                        e
                );
            }
        }
    }
}