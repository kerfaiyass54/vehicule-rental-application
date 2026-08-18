package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LocationAdminDTO {

    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String country;

    @NotBlank
    private String position;
}