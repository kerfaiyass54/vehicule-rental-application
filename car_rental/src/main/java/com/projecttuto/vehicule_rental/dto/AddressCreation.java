package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AddressCreation {

    @NotBlank
    private String road;

    @NotNull
    @Positive
    private Integer number;

    @NotBlank
    private String location;
}