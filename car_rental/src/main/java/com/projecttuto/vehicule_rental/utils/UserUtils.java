package com.projecttuto.vehicule_rental.utils;

import com.projecttuto.vehicule_rental.DTO.LoginInfoDTO;
import com.projecttuto.vehicule_rental.entities.Admin;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import org.springframework.stereotype.Component;

@Component
public class UserUtils {

    public Admin mapToAdmin(LoginInfoDTO loginInfoDTO){
        Admin  admin = new Admin();
        admin.setEmail(loginInfoDTO.getEmail());
        admin.setAdminName(loginInfoDTO.getUsername());
        return admin;
    }

    public Client mapToClient(LoginInfoDTO loginInfoDTO){
        Client client = new Client();
        client.setEmail(loginInfoDTO.getEmail());
        client.setNameClient(loginInfoDTO.getUsername());
        client.setRole(loginInfoDTO.getRole());
        return client;
    }

    public Supplier mapToSupplier(LoginInfoDTO loginInfoDTO){
        Supplier supplier = new Supplier();
        supplier.setEmail(loginInfoDTO.getEmail());
        supplier.setSuppName(loginInfoDTO.getUsername());
        supplier.setRole(loginInfoDTO.getRole());
        return supplier;
    }

    public Repair mapToRepair(LoginInfoDTO loginInfoDTO){
        Repair repair = new Repair();
        repair.setEmail(loginInfoDTO.getEmail());
        repair.setNameRepair(loginInfoDTO.getUsername());
        repair.setRole(loginInfoDTO.getRole());
        return repair;
    }
}
