package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RepairDTO {

    @NotNull
    private Long idRepair;

    @NotBlank
    private String nameRepair;

    @NotBlank
    private String locationName;

    @NotBlank
    @Email
    private String email;

    private String pass;

    @NotBlank
    private String role;
}