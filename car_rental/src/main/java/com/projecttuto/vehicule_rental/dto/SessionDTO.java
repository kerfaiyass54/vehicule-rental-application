package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class SessionDTO {

    @NotBlank
    private String id;

    @NotBlank
    private String userId;

    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private Instant sessionStart;

    @NotBlank
    private String ipAddress;

    @NotBlank
    private String userAgent;

    @NotBlank
    private String deviceType;

    @NotBlank
    private String country;

    @NotBlank
    private String city;

    @DecimalMin(value = "0.0")
    private double riskScore;

    private boolean suspicious;

    private String suspiciousReason;
}