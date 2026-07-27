package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.BuyStatus;

import java.time.Instant;

public class BuyingPageDTO {
    private long idBuying;
    private Instant dateBuy;
    private int periodBuy;
    private BuyStatus buyStatus;
    private String vehiculeName;
}
