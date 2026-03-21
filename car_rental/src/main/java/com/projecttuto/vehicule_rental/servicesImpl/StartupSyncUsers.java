package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.entities.Admin;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.repositories.AdminRepository;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class StartupSyncUsers implements CommandLineRunner {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private KeycloakAdminServiceImpl keycloakUserSyncService;

    @Override
    public void run(String... args) {
        List<Client> clients = clientRepository.findAll();
        List<Supplier> suppliers = supplierRepository.findAll();
        List<Admin> admins = adminRepository.findAll();
        List<Repair> repairs = repairRepository.findAll();
        for (Client c : clients) {
            keycloakUserSyncService.syncUserToKeycloak(
                    c.getNameClient().toLowerCase().replaceAll("[^a-z0-9._-]", "_"),
                    c.getNameClient(),c.getNameClient(),
                    c.getEmail(),
                    c.getPass(),
                    c.getRole()
            );
        }

        for (Supplier s : suppliers) {
            keycloakUserSyncService.syncUserToKeycloak(
                  s.getSuppName().toLowerCase().replaceAll("[^a-z0-9._-]", "_"),s.getSuppName(),
                    s.getSuppName(),
                  s.getEmail(),
                  s.getPass(),
                  "supplier"
            );
        }

        for (Admin a : admins) {
            keycloakUserSyncService.syncUserToKeycloak(
              a.getAdminName().toLowerCase().replaceAll("[^a-z0-9._-]", "_"),
                    a.getAdminName(),a.getAdminName(),
              a.getEmail(),
              a.getPass(),
              "admin"
            );
        }

        for (Repair r : repairs) {
            keycloakUserSyncService.syncUserToKeycloak(
                    r.getNameRepair().toLowerCase().replaceAll("[^a-z0-9._-]", "_"),
                    r.getNameRepair(),r.getNameRepair(),
                    r.getEmail(),
                    r.getPass(),
                    r.getRole()
            );
        }



    }
}
