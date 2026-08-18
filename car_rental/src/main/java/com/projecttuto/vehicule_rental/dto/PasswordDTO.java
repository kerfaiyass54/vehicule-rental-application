package com.projecttuto.vehicule_rental.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PasswordDTO {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String role;
}