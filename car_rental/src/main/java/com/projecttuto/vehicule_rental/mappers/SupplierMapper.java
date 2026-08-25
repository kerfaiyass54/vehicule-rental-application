package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.dto.SupplierInfoDTO;
import com.projecttuto.vehicule_rental.entities.Supplier;
import org.springframework.stereotype.Component;

@Component
public class SupplierMapper {

    public SupplierInfoDTO toInfoDTO(Supplier supplier) {

        if (supplier == null) {
            return null;
        }

        SupplierInfoDTO dto = new SupplierInfoDTO();

        dto.setIdSupp(supplier.getIdSupplier());
        dto.setSuppName(supplier.getSupplierName());
        dto.setNationality(supplier.getNationality());
        dto.setEmail(supplier.getEmail());

        dto.setNumberVehicules(
                supplier.getVehicles() != null
                        ? supplier.getVehicles().size()
                        : 0
        );

        dto.setNumberSubscriptions(
                supplier.getSubscriptions() != null
                        ? supplier.getSubscriptions().size()
                        : 0
        );

        dto.setNumberBuyings(
                supplier.getBuyings() != null
                        ? supplier.getBuyings().size()
                        : 0
        );

        return dto;
    }
}