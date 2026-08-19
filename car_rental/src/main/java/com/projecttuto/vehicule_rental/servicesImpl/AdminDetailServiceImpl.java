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
    public void updateDetails(AdminDTO admin, Long id){
        Admin a = adminRepository.findAdminByIdAdmin(id);
        a.setAdminName(admin.getAdminName());
        a.setEmail(admin.getEmail());
        a.setPasswordHash(admin.getPassword());
        adminRepository.save(a);
    }

    @Override
    public AdminDTO getDetails(Long id){
        Admin admin = adminRepository.findAdminByIdAdmin(id);
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setAdminName(admin.getAdminName());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setPassword(admin.getPasswordHash());
        return adminDTO;
    }

    @Override
    public AdminDashboardDTO getDashboard() {

        AdminDashboardDTO dto = new AdminDashboardDTO();

        dto.setTotalClients(clientRepository.count());

        dto.setTotalSuppliers(supplierRepository.count());

        dto.setTotalRepairs(repairRepository.count());

        dto.setTotalLocations(locationRepository.count());

        dto.setTotalVehicles(vehiculeRepository.count());

        dto.setTotalBuyings(buyingRepository.count());

        dto.setTotalSubscriptions(subscriptionRepository.count());

        dto.setTotalTickets(ticketRepository.count());

        dto.setTotalDemands(demandRepository.count());

        dto.setActiveRepairs(
                repairInfoRepository.countByRepairStatus(
                        RepairStatus.PENDING_FINISH));

        return dto;
    }
}
