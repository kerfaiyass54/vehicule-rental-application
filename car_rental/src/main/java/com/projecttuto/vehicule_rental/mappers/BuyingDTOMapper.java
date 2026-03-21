package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.BuyingDTO;
import com.projecttuto.vehicule_rental.entities.Buying;
import org.springframework.stereotype.Component;


@Component
public class BuyingDTOMapper {

    public BuyingDTO mapToDTO(Buying buying) {
        BuyingDTO buyingDTO = new BuyingDTO();
        buyingDTO.setIdBuying(buying.getIdBuying());
        buyingDTO.setBuyStatus(buying.getBuyStatus());
        buyingDTO.setDateBuy(buying.getDateBuy());
        return buyingDTO;
    }
}
