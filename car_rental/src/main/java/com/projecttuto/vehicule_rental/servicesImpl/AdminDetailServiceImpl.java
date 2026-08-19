package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.AdminDTO;
import com.projecttuto.vehicule_rental.dto.AdminDashboardDTO;
import com.projecttuto.vehicule_rental.entities.Admin;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.services.AdminDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminDetailServiceImpl implements AdminDetailsService {

    private final VehiculeRepository vehiculeRepository;
    private final BuyingRepository buyingRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final TicketRepository ticketRepository;
    private final DemandRepository demandRepository;
    private final RepairInfoRepository repairInfoRepository;
    private final AdminRepository adminRepository;
    private final ClientRepository clientRepository;
    private final SupplierRepository supplierRepository;
    private final LocationRepository locationRepository;
    private final RepairRepository repairRepository;

    @Override
    public void updateDetails(AdminDTO adminDTO, Long id) {

        Admin admin = findAdminById(id);

        updateAdminFields(admin, adminDTO);

        saveAdmin(admin);
    }

    @Override
    public AdminDTO getDetails(Long id) {

        Admin admin = findAdminById(id);

        return mapToAdminDTO(admin);
    }

    @Override
    public AdminDashboardDTO getDashboard() {

        AdminDashboardDTO dto = new AdminDashboardDTO();

        setGeneralStatistics(dto);
        setRepairStatistics(dto);

        return dto;
    }

    // -------------------------------------------------------------------------
    // Admin
    // -------------------------------------------------------------------------

    private Admin findAdminById(Long id) {

        Admin admin = adminRepository.findAdminByIdAdmin(id);

        if (admin == null) {
            throw new RuntimeException("Admin not found with id: " + id);
        }

        return admin;
    }

    private void updateAdminFields(
            Admin admin,
            AdminDTO adminDTO) {

        admin.setAdminName(adminDTO.getAdminName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPasswordHash(adminDTO.getPassword());
    }

    private void saveAdmin(Admin admin) {

        adminRepository.save(admin);
    }

    private AdminDTO mapToAdminDTO(Admin admin) {

        AdminDTO dto = new AdminDTO();

        dto.setAdminName(admin.getAdminName());
        dto.setEmail(admin.getEmail());
        dto.setPassword(admin.getPasswordHash());

        return dto;
    }

    // -------------------------------------------------------------------------
    // Dashboard
    // -------------------------------------------------------------------------

    private void setGeneralStatistics(AdminDashboardDTO dto) {

        dto.setTotalClients(clientRepository.count());
        dto.setTotalSuppliers(supplierRepository.count());
        dto.setTotalRepairs(repairRepository.count());
        dto.setTotalLocations(locationRepository.count());
        dto.setTotalVehicles(vehiculeRepository.count());
        dto.setTotalBuyings(buyingRepository.count());
        dto.setTotalSubscriptions(subscriptionRepository.count());
        dto.setTotalTickets(ticketRepository.count());
        dto.setTotalDemands(demandRepository.count());
    }

    private void setRepairStatistics(AdminDashboardDTO dto) {

        dto.setActiveRepairs(
                repairInfoRepository.countByRepairStatus(
                        RepairStatus.PENDING_FINISH
                )
        );
    }
}