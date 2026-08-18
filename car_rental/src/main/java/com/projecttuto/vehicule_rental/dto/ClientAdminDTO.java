package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClientAdminDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String nameClient;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nationality;

    @NotNull
    @DecimalMin(value = "0.0")
    private double budget;

    @NotNull
    private Long locationId;

    @NotBlank
    private String locationName;
}