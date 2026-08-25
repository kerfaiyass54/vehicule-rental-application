package com.projecttuto.vehicule_rental.dto;

import com.projecttuto.vehicule_rental.enums.BuyStatus;
import lombok.Data;

import java.time.Instant;

@Data
public class BuyingDTO {

    private Long idBuying;

    private Instant dateBuy;

    private Integer periodBuy;

    private BuyStatus buyStatus;

    private boolean renew;

    // Vehicle information
    private String vehiculeName;

    private String supplierName;
}