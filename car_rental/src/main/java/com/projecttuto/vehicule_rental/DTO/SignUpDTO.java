package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.UserRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor

public class SignUpDTO {
    private String name;
    private String email;
    private String password;
    private UserRole role;

}
