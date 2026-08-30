package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.LocationDTO;
import com.projecttuto.vehicule_rental.dto.RepairDashboardDTO;
import com.projecttuto.vehicule_rental.dto.RepairProfileDTO;

import java.util.List;

public interface RepairDetailsService {

    RepairDashboardDTO getDashboard(String repairEmail);
    RepairProfileDTO getInfo(String repairEmail);
    RepairProfileDTO updateLocation(
            String repairEmail,
            Long locationId);
    List<LocationDTO> getLocations();


}
