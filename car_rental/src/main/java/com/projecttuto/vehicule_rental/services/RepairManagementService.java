package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.RepairAdminDTO;
import org.springframework.data.domain.Page;

public interface RepairManagementService {
    Page<RepairAdminDTO> getRepairs(int page, int size);

    RepairAdminDTO getRepair(Long id);

    RepairAdminDTO updateRepair(Long id, RepairAdminDTO dto);

    void deleteRepair(Long id);
}
