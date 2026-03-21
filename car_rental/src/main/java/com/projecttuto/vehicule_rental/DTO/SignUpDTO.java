package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.RoleUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class SignUpDTO {
    private String name;
    private String email;
    private String password;
    private RoleUser role;

}
