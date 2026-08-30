package com.projecttuto.vehicule_rental.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketClientDTO {

    private String clientName;

    private String nationality;

    private String email;

    private String locationName;
}