package com.projecttuto.vehicule_rental.DTO;


import com.projecttuto.vehicule_rental.enums.BuyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
public class BuyingInfoDTO {

    private long idBuying;
    private Instant dateBuy;
    private int periodBuy;
    private BuyStatus buyStatus;
    private String vehiculeName;
    private String clientEmail;
}
