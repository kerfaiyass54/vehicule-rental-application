package com.projecttuto.vehicule_rental.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class UserDTO {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
