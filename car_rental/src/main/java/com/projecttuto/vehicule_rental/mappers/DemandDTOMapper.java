package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.DemandDTO;
import com.projecttuto.vehicule_rental.entities.Demand;
import org.springframework.stereotype.Component;


@Component
public class DemandDTOMapper {

    public DemandDTO mapToDTO(Demand demand) {
        DemandDTO dto = new DemandDTO();
        dto.setIdDemand(demand.getIdDemand());
        dto.setStatusConfirm(demand.getStatusConfirm());
        dto.setType(demand.getType());
        dto.setStatusConfirm(demand.getStatusConfirm());
        return dto;
    }
}
