package com.projecttuto.vehicule_rental.DTO;

import com.projecttuto.vehicule_rental.enums.BuyStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class BuyingDTO {
    private long idBuying;
    private Instant dateBuy;
    private BuyStatus buyStatus;

}
