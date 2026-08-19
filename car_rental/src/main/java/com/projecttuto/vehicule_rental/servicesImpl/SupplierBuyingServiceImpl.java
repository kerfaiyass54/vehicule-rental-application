package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.BuyingResponseDTO;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.repositories.BuyingRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.SupplierBuyingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierBuyingServiceImpl implements SupplierBuyingService {

    private final SupplierRepository supplierRepository;

    private final BuyingRepository buyingRepository;

    @Override
    public Page<BuyingResponseDTO> checkBuyings(
            String supplierEmail,
            int page,
            int size) {

        Supplier supplier = supplierRepository.findSupplierByEmail(supplierEmail);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return buyingRepository.findByVehiculeSupplier(supplier, pageable)
                .map(buying -> {

                    BuyingResponseDTO dto = new BuyingResponseDTO();

                    dto.setIdBuying(buying.getIdBuying());

                    dto.setVehiculeName(
                            buying.getVehicle().getVehicleName());

                    dto.setClientName(
                            buying.getClient().getClientName());

                    dto.setClientEmail(
                            buying.getClient().getEmail());

                    dto.setDateBuy(
                            buying.getDateBuy());

                    dto.setPeriod(
                            buying.getPeriodBuy());

                    dto.setStatus(
                            buying.getBuyStatus());

                    return dto;
                });

    }

}
