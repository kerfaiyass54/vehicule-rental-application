package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.RepairDTO;
import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;

public interface RepairInfoService {

    RepairInfoDTO getRepairInfoById(Long id);

}
