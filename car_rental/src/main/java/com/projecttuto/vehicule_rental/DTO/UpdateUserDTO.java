package com.projecttuto.vehicule_rental.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class UpdateUserDTO {

    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String newEmail;
}
