package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import org.springframework.data.domain.Page;

import java.util.List;

public interface RepairOperationsService {
    Page<RepairInfoDTO> getRepairInfos(
            String repairEmail,
            int page,
            int size);
    RepairInfoDTO startRepair(Long repairInfoId);

    void cancelRepair(Long repairInfoId);
    List<RepairInfo> getRepairInfo(String repairName);

    RepairInfoDTO getRepairInfo(Long repairInfoId);








}
