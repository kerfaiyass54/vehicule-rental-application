package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.TicketType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OpenTicketDTO {

    @NotNull
    private TicketType type;

    @NotBlank
    private String decription;

    @NotBlank
    private String repairName;

    @NotBlank
    @Email
    private String clientEmail;

    @NotBlank
    private String vehiculeName;
}