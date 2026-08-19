package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.SupplierAdminDTO;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.SupplierManagementService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SupplierManagementServiceImpl implements SupplierManagementService {

    private final SupplierRepository supplierRepository;


    @Override
    public Page<SupplierAdminDTO> getSuppliers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return supplierRepository.findAll(pageable)
                .map(supplier -> {

                    SupplierAdminDTO dto = new SupplierAdminDTO();

                    dto.setId(supplier.getIdSupplier());

                    dto.setSuppName(supplier.getSupplierName());

                    dto.setEmail(supplier.getEmail());

                    dto.setNationality(supplier.getNationality());

                    dto.setExperience(supplier.getExperience());



                    return dto;
                });
    }

    @Override
    public SupplierAdminDTO getSupplier(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Supplier not found"));

        SupplierAdminDTO dto = new SupplierAdminDTO();

        dto.setId(supplier.getIdSupplier());

        dto.setSuppName(supplier.getSupplierName());

        dto.setEmail(supplier.getEmail());

        dto.setNationality(supplier.getNationality());

        dto.setExperience(supplier.getExperience());


        return dto;
    }

    @Override
    public SupplierAdminDTO updateSupplier(Long id,
                                           SupplierAdminDTO dto) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Supplier not found"));

        supplier.setSupplierName(dto.getSuppName());

        supplier.setNationality(dto.getNationality());

        supplier.setExperience(dto.getExperience());

        Supplier saved = supplierRepository.save(supplier);

        SupplierAdminDTO response = new SupplierAdminDTO();

        response.setId(saved.getIdSupplier());

        response.setSuppName(saved.getSupplierName());

        response.setEmail(saved.getEmail());

        response.setNationality(saved.getNationality());

        response.setExperience(saved.getExperience());


        return response;
    }

    @Override
    public void deleteSupplier(Long id) {

        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Supplier not found"));

        supplierRepository.delete(supplier);

    }

}
