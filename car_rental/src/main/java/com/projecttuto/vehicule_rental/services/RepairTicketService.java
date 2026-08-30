package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.*;
import org.springframework.data.domain.Page;

public interface RepairTicketService {
    Page<RepairTicketDTO> getTickets(
            String repairEmail,
            int page,
            int size);

    Page<TicketListDTO> getTickets(int page, int size, String email);

    TicketClientDTO getClient(Long ticketId);

    TicketVehiculeDTO getVehicule(Long ticketId);

    TicketDetailsDTO getTicketInfo(Long ticketId);

}
