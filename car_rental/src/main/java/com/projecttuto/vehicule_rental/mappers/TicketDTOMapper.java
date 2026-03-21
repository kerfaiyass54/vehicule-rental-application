package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.TicketDTO;
import com.projecttuto.vehicule_rental.entities.Ticket;
import org.springframework.stereotype.Component;


@Component
public class TicketDTOMapper {
    public TicketDTO mapToDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setIdTicket(ticket.getIdTicket());
        ticketDTO.setType(ticket.getType());
        ticketDTO.setTarif(ticket.getTarif());
        ticketDTO.setStatus(ticket.getStatus());
        ticketDTO.setDateInsert(ticket.getDateInsert());
        return ticketDTO;
    }
}
