package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import org.springframework.stereotype.Component;


@Component
public class RepairInfoDTOMapper {

    public RepairInfoDTO mapToDTO(RepairInfo repairInfo) {
        RepairInfoDTO repairInfoDTO = new RepairInfoDTO();
        repairInfoDTO.setDateStart(repairInfo.getDateStart());
        repairInfoDTO.setRepairStatus(repairInfo.getRepairStatus());
        repairInfoDTO.setDateStart(repairInfo.getDateStart());
        return repairInfoDTO;
    }
}
