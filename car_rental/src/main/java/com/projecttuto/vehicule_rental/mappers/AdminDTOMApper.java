package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.AdminDTO;
import com.projecttuto.vehicule_rental.entities.Admin;
import org.springframework.stereotype.Component;


@Component
public class AdminDTOMApper {

    public AdminDTO getAdminDTO(Admin admin) {
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setAdminName(admin.getAdminName());
        adminDTO.setIdAdmin(admin.getIdAdmin());
        return adminDTO;
    }
}
