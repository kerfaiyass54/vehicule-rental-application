package com.projecttuto.vehicule_rental.DTO;


import com.projecttuto.vehicule_rental.enums.TicketType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OpenTicketDTO {
    private TicketType type;
    private String decription;
    private String repairName;
    private String clientEmail;
    private String vehiculeName;

}
