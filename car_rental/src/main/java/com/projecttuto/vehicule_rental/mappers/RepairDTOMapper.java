package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.RepairDTO;
import com.projecttuto.vehicule_rental.entities.Repair;
import org.springframework.stereotype.Component;


@Component
public class RepairDTOMapper {

    public RepairDTO mapToDTORepair(Repair repair) {
        RepairDTO dto = new RepairDTO();
        dto.setIdRepair(repair.getIdRepair());
        dto.setNameRepair(repair.getNameRepair());
        return dto;
    }
}
