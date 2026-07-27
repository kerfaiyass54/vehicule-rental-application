package com.projecttuto.vehicule_rental.DTO;


import com.projecttuto.vehicule_rental.enums.TypeTicket;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OpenTicketDTO {

    private TypeTicket type;
    private String decription;
    private String repairName;
    private String clientEmail;
    private String vehiculeName;

}
