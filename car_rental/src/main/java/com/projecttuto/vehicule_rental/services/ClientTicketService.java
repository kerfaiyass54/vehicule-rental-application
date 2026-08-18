package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.OpenTicketDTO;
import com.projecttuto.vehicule_rental.dto.TicketInfoDTO;
import org.springframework.data.domain.Page;

public interface ClientTicketService {
    TicketInfoDTO openTicket(OpenTicketDTO openTicketDTO);

    Page<TicketInfoDTO> getClientTickets(String clientEmail, int page, int size);
}
