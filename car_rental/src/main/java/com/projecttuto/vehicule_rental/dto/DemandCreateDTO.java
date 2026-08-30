package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.DemandType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DemandCreateDTO {

    @NotNull
    private DemandType type;

    @NotNull
    @Positive
    private Integer estimatedTime;

    @NotNull
    @Positive
    private Long ticketId;

    @NotBlank
    @Email
    private String supplierEmail;

    @NotBlank
    private String vehiculeName;
}