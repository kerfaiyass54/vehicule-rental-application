package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.BuyStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class BuyingResponseDTO {

    @NotNull
    private Long idBuying;

    @NotBlank
    private String vehiculeName;

    @NotBlank
    private String clientName;

    @NotBlank
    @Email
    private String clientEmail;

    @NotNull
    private Instant dateBuy;

    @Min(1)
    private int period;

    @NotNull
    private BuyStatus status;
}

