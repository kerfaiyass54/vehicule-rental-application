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
public class SubscriptionResponseDTO {

    @NotNull
    @Positive
    private Long idSubscription;

    @NotBlank
    private String clientName;

    @NotBlank
    @Email
    private String clientEmail;

    @NotNull
    private SubscriptionType type;

    @NotNull
    private Instant dateStart;

    @DecimalMin(value = "0.0")
    private double price;

    @Positive
    private int reduce;
}