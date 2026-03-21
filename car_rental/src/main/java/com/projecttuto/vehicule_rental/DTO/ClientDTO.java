package com.projecttuto.vehicule_rental.DTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ClientDTO {
    private long idClient;
    private String nameClient;
    private String nationality;
    private double budget;
    private String locationName;
    private String email;
    private String role;


}
