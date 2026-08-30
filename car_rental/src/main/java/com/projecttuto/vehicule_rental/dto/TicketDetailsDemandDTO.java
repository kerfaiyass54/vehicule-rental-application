package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDetailsDemandDTO {

    private TicketType type;

    private String description;

    private Instant date;

    private Double tariff;

    private String clientEmail;

    private String vehiculeName;
}