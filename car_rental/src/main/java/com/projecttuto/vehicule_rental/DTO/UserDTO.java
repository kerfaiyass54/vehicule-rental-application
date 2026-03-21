package com.projecttuto.vehicule_rental.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserDTO {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
}
