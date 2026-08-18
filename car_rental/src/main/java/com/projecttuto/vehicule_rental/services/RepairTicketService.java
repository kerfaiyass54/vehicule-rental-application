package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.RepairTicketDTO;
import org.springframework.data.domain.Page;

public interface RepairTicketService {
    Page<RepairTicketDTO> getTickets(
            String repairEmail,
            int page,
            int size);

}
