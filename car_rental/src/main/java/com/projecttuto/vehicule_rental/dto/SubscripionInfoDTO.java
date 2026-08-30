package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.SubscriptionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class SubscripionInfoDTO {

    @Positive
    private Long idSubscrip;

    @NotNull
    private SubscriptionType type;

    private Instant dateStart;

    @Positive
    private int reduce;

    @DecimalMin(value = "0.0")
    private double price;

    @NotBlank
    private Long idSupplier;

    @NotBlank
    @Email
    private String clientEmail;
}