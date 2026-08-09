package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.DTO.*;
import org.springframework.data.domain.Page;

import java.util.List;

public interface AdminService {


    void updateDetails(AdminDTO adminDTO, Long id);
    AdminDTO getDetails(Long id);
    Page<ClientAdminDTO> getClients(int page, int size);

    ClientAdminDTO getClient(Long id);

    ClientAdminDTO updateClient(Long id, ClientAdminDTO dto);

    void deleteClient(Long id);

    Page<SupplierAdminDTO> getSuppliers(int page, int size);

    SupplierAdminDTO getSupplier(Long id);

    AdminDashboardDTO getDashboard();

    SupplierAdminDTO updateSupplier(Long id, SupplierAdminDTO dto);

    void deleteSupplier(Long id);

    Page<RepairAdminDTO> getRepairs(int page, int size);

    RepairAdminDTO getRepair(Long id);

    RepairAdminDTO updateRepair(Long id, RepairAdminDTO dto);

    void deleteRepair(Long id);

    LocationAdminDTO createLocation(LocationAdminDTO dto);

    Page<LocationAdminDTO> getLocations(int page, int size);

    LocationAdminDTO getLocation(Long id);

    LocationAdminDTO updateLocation(Long id, LocationAdminDTO dto);

    void deleteLocation(Long id);

}
