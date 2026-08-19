package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.SupplierDashboardDTO;
import com.projecttuto.vehicule_rental.dto.SupplierDetailsDTO;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.enums.BuyStatus;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.services.SupplierDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierDetailsServiceImpl implements SupplierDetailsService {

    private final SupplierRepository supplierRepository;

    private final BuyingRepository buyingRepository;
    private final VehiculeRepository vehiculeRepository;

    private final TicketRepository ticketRepository;

    private final SubscriptionRepository subscriptionRepository;

    private final DemandRepository demandRepository;


    @Override
    public SupplierDashboardDTO getDashboard(String supplierEmail) {

        Supplier supplier = supplierRepository.findSupplierByEmail(supplierEmail);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        SupplierDashboardDTO dto = new SupplierDashboardDTO();

        dto.setSupplierName(supplier.getSupplierName());

        dto.setTotalVehicles(
                (Long) vehiculeRepository.countBySupplier(supplier));

        dto.setTotalBuyings(
                (Long) buyingRepository.countByVehiculeSupplier(supplier));

        dto.setActiveBuyings(
                (Long) buyingRepository.countByVehiculeSupplierAndBuyStatus(
                        supplier,
                        BuyStatus.BEING_USED));

        dto.setTotalSubscriptions(
                (Long) subscriptionRepository.countBySupplier(supplier));

        dto.setTotalDemands(
                (Long) demandRepository.countBySupplier(supplier));

        dto.setApprovedDemands(
                (Long) demandRepository.countBySupplierAndStatusConfirm(
                        supplier,
                        ConfirmStatus.APPROVED));

        dto.setRefusedDemands(
                (Long) demandRepository.countBySupplierAndStatusConfirm(
                        supplier,
                        ConfirmStatus.REFUSED));

        dto.setPendingDemands(
                (Long) demandRepository.countBySupplierAndStatusConfirm(
                        supplier,
                        ConfirmStatus.PENDING));

        return dto;
    }




    @Override
    public SupplierDetailsDTO getDetails(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null)
            throw new RuntimeException("Supplier not found");

        SupplierDetailsDTO dto = new SupplierDetailsDTO();

        dto.setExperience(supplier.getExperience());
        dto.setNationality(supplier.getNationality());
        dto.setEmail(email);
        dto.setSuppName(supplier.getSupplierName());

        return dto;
    }
}
