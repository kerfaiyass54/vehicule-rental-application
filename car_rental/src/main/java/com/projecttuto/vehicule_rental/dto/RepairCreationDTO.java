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
public class RepairCreationDTO {

    @NotBlank(message = "Repair name is required")
    private String repairName;

    @NotBlank(message = "Role is required")
    private String role;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Location is required")
    @Positive(message = "Location ID must be positive")
    private Long locationId;

    @NotNull(message = "Admin is required")
    @Positive(message = "Admin ID must be positive")
    private Long adminId;
}