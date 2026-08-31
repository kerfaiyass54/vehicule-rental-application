package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.CreateDemandDTO;
import com.projecttuto.vehicule_rental.dto.DemandDetailsDTO;
import com.projecttuto.vehicule_rental.dto.DemandsListPageDTO;
import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import org.springframework.data.domain.Page;

public interface RepairDemandService {
    RepairTicketDTO createDemand(CreateDemandDTO dto);

    Page<DemandsListPageDTO> getDemands(int size, int page, String repairEmail);

    DemandDetailsDTO getDemandDetails(Long demandId);

    String getSupplierEmail(Long vehiculeId);




}
