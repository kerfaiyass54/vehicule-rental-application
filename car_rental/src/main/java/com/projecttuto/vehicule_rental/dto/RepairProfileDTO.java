package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepairProfileDTO {

    @NotNull
    @Positive
    private Long idRepair;

    @NotBlank
    private String nameRepair;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String role;

    @NotBlank
    private String locationName;

    @NotBlank
    private String country;

    @NotBlank
    private String position;
}