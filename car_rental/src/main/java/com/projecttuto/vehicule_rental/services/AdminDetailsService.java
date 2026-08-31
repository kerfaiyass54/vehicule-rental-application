package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.AdminDTO;
import com.projecttuto.vehicule_rental.dto.AdminDashboardDTO;

public interface AdminDetailsService {
    void updateDetails(AdminDTO adminDTO, String email);
    AdminDTO getDetails(String email);
    AdminDashboardDTO getDashboard();

}
