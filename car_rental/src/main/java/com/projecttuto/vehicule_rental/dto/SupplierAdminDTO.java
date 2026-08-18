package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SupplierAdminDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String suppName;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String nationality;

    @PositiveOrZero
    private int experience;

    @NotBlank
    private String role;
}