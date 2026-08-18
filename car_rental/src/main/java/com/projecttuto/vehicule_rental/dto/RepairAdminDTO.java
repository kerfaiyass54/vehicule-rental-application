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
public class RepairAdminDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String nameRepair;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String role;

    @NotNull
    private Long locationId;

    @NotBlank
    private String locationName;
}