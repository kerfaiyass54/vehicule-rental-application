package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CreateDemandDTO {

    @NotNull
    private Long ticketId;

    @NotBlank
    private String repairEmail;

    @NotBlank
    private String supplierName;

    @NotBlank
    private String type;

    @Min(1)
    private int estimatedTime;
}