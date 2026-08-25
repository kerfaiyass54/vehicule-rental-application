package com.projecttuto.vehicule_rental.services.impl;

import com.projecttuto.vehicule_rental.dto.SupplierInfoDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeSupplierDTO;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import com.projecttuto.vehicule_rental.mappers.SupplierMapper;
import com.projecttuto.vehicule_rental.mappers.VehiculeMapper;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.ClientSupplierService;
import com.projecttuto.vehicule_rental.specifications.SupplierSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ClientSupplierServiceImpl implements ClientSupplierService {

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;
    private final VehiculeRepository vehiculeRepository;
    private final VehiculeMapper vehiculeMapper;

    @Override
    public Page<VehiculeSupplierDTO> getAvailableVehiculesBySupplier(
            Long supplierId,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return vehiculeRepository
                .findBySupplier_IdSuppAndVehicleStatus(
                        supplierId,
                        VehiculeStatus.AVAILABLE,
                        pageable
                )
                .map(vehiculeMapper::toSupplierDTO);
    }


    @Override
    public Page<SupplierInfoDTO> searchSuppliers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return supplierRepository
                .findAll(pageable)
                .map(supplierMapper::toInfoDTO);
    }


    @Override
    public Page<SupplierInfoDTO> searchSuppliers(
            String keyword,
            int page,
            int size
    ) {

        Pageable pageable = PageRequest.of(page, size);

        return supplierRepository
                .findAll(
                        SupplierSpecification.search(keyword),
                        pageable
                )
                .map(supplierMapper::toInfoDTO);
    }
}