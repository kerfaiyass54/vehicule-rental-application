package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.UserDTO;

public interface AuthenticationService {
    void signUpUser(UserDTO user);
    boolean isEmailExist(String email);

}
