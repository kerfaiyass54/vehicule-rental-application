package com.projecttuto.vehicule_rental.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserLoginDataDTO {
    private String username;
    private String email;
}
