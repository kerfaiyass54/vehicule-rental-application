package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.*;
import com.projecttuto.vehicule_rental.entities.*;
import com.projecttuto.vehicule_rental.enums.RepairStatus;

import com.projecttuto.vehicule_rental.repositories.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.services.AdminService;

@Service
@AllArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private com.projecttuto.vehicule_rental.repositories.RepairRepository repairRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private LocationRepository locationRepository;

    
    private VehiculeRepository vehiculeRepository;
    private BuyingRepository buyingRepository;
    private SubscriptionRepository subscriptionRepository;
    private TicketRepository ticketRepository;
    private DemandRepository demandRepository;
    private RepairInfoRepository repairInfoRepository;


    @Override
    public Page<RepairAdminDTO> getRepairs(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return repairRepository.findAll(pageable)
                .map(repair -> {

                    RepairAdminDTO dto = new RepairAdminDTO();

                    dto.setId(repair.getIdRepair());

                    dto.setNameRepair(repair.getNameRepair());

                    dto.setEmail(repair.getEmail());


                    if (repair.getLocation() != null) {
                        dto.setLocationId(repair.getLocation().getIdLoc());
                        dto.setLocationName(repair.getLocation().getName());
                    }

                    return dto;
                });
    }

    @Override
    public LocationAdminDTO createLocation(LocationAdminDTO dto) {

        Location location = new Location();

        location.setName(dto.getName());

        location.setCountry(dto.getCountry());

        location.setPosition(dto.getPosition());

        Location saved = locationRepository.save(location);

        LocationAdminDTO response = new LocationAdminDTO();

        response.setId(saved.getIdLoc());

        response.setName(saved.getName());

        response.setCountry(saved.getCountry());

        response.setPosition(saved.getPosition());

        return response;
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

    @Override
    public Page<LocationAdminDTO> getLocations(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return locationRepository.findAll(pageable)
                .map(location -> {

                    LocationAdminDTO dto = new LocationAdminDTO();

                    dto.setId(location.getIdLoc());

                    dto.setName(location.getName());

                    dto.setCountry(location.getCountry());

                    dto.setPosition(location.getPosition());

                    return dto;
                });
    }

    @Override
    public LocationAdminDTO getLocation(Long id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));

        LocationAdminDTO dto = new LocationAdminDTO();

        dto.setId(location.getIdLoc());

        dto.setName(location.getName());

        dto.setCountry(location.getCountry());

        dto.setPosition(location.getPosition());

        return dto;
    }

    @Override
    public LocationAdminDTO updateLocation(Long id,
                                           LocationAdminDTO dto) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));

        location.setName(dto.getName());

        location.setCountry(dto.getCountry());

        location.setPosition(dto.getPosition());

        Location saved = locationRepository.save(location);

        LocationAdminDTO response = new LocationAdminDTO();

        response.setId(saved.getIdLoc());

        response.setName(saved.getName());

        response.setCountry(saved.getCountry());

        response.setPosition(saved.getPosition());

        return response;
    }

    @Override
    public void deleteLocation(Long id) {

        Location location = locationRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));

        locationRepository.delete(location);

    }



    @Override
    public Page<SupplierAdminDTO> getSuppliers(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return supplierRepository.findAll(pageable)
                .map(supplier -> {

                    SupplierAdminDTO dto = new SupplierAdminDTO();

                    dto.setId(supplier.getIdSupp());

                    dto.setSuppName(supplier.getSuppName());

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

        dto.setId(supplier.getIdSupp());

        dto.setSuppName(supplier.getSuppName());

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

        supplier.setSuppName(dto.getSuppName());

        supplier.setNationality(dto.getNationality());

        supplier.setExperience(dto.getExperience());

        Supplier saved = supplierRepository.save(supplier);

        SupplierAdminDTO response = new SupplierAdminDTO();

        response.setId(saved.getIdSupp());

        response.setSuppName(saved.getSuppName());

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

    @Override
    public Page<ClientAdminDTO> getClients(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return clientRepository.findAll(pageable)
                .map(client -> {

                    ClientAdminDTO dto = new ClientAdminDTO();

                    dto.setId(client.getIdClient());

                    dto.setNameClient(client.getNameClient());

                    dto.setEmail(client.getEmail());

                    dto.setNationality(client.getNationality());

                    dto.setBudget(client.getBudget());

                    if(client.getLocation()!=null){
                        dto.setLocationId(client.getLocation().getIdLoc());
                        dto.setLocationName(client.getLocation().getName());
                    }

                    return dto;

                });

    }

    @Override
    public RepairAdminDTO updateRepair(Long id,
                                       RepairAdminDTO dto) {

        Repair repair = repairRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Repair center not found"));

        repair.setNameRepair(dto.getNameRepair());

        if (dto.getLocationId() != null) {

            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() ->
                            new RuntimeException("Location not found"));

            repair.setLocation(location);
        }

        Repair saved = repairRepository.save(repair);

        RepairAdminDTO response = new RepairAdminDTO();

        response.setId(saved.getIdRepair());

        response.setNameRepair(saved.getNameRepair());

        response.setEmail(saved.getEmail());


        if (saved.getLocation() != null) {
            response.setLocationId(saved.getLocation().getIdLoc());
            response.setLocationName(saved.getLocation().getName());
        }

        return response;
    }

    @Override
    public void deleteRepair(Long id) {

        Repair repair = repairRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Repair center not found"));

        repairRepository.delete(repair);
    }

    @Override
    public RepairAdminDTO getRepair(Long id) {

        Repair repair = repairRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Repair center not found"));

        RepairAdminDTO dto = new RepairAdminDTO();

        dto.setId(repair.getIdRepair());

        dto.setNameRepair(repair.getNameRepair());

        dto.setEmail(repair.getEmail());


        if (repair.getLocation() != null) {
            dto.setLocationId(repair.getLocation().getIdLoc());
            dto.setLocationName(repair.getLocation().getName());
        }

        return dto;
    }




    @Override
    public void updateDetails(AdminDTO admin, Long id){
        Admin a = adminRepository.findAdminByIdAdmin(id);
        a.setAdminName(admin.getAdminName());
        a.setEmail(admin.getEmail());
        a.setPass(admin.getPassword());
        adminRepository.save(a);
    }

    @Override
    public AdminDTO getDetails(Long id){
        Admin admin = adminRepository.findAdminByIdAdmin(id);
        AdminDTO adminDTO = new AdminDTO();
        adminDTO.setAdminName(admin.getAdminName());
        adminDTO.setEmail(admin.getEmail());
        adminDTO.setPassword(admin.getPass());
        return adminDTO;
    }


    @Override
    public ClientAdminDTO getClient(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        ClientAdminDTO dto = new ClientAdminDTO();

        dto.setId(client.getIdClient());

        dto.setNameClient(client.getNameClient());

        dto.setEmail(client.getEmail());

        dto.setNationality(client.getNationality());

        dto.setBudget(client.getBudget());

        if(client.getLocation()!=null){
            dto.setLocationId(client.getLocation().getIdLoc());
            dto.setLocationName(client.getLocation().getName());
        }

        return dto;

    }

    @Override
    public ClientAdminDTO updateClient(Long id, ClientAdminDTO dto) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        client.setNameClient(dto.getNameClient());

        client.setNationality(dto.getNationality());

        client.setBudget(dto.getBudget());

        if(dto.getLocationId()!=null){

            Location location = locationRepository.findById(dto.getLocationId())
                    .orElseThrow(() ->
                            new RuntimeException("Location not found"));

            client.setLocation(location);

        }

        Client saved = clientRepository.save(client);

        ClientAdminDTO response = new ClientAdminDTO();

        response.setId(saved.getIdClient());

        response.setNameClient(saved.getNameClient());

        response.setEmail(saved.getEmail());

        response.setNationality(saved.getNationality());

        response.setBudget(saved.getBudget());

        if(saved.getLocation()!=null){
            response.setLocationId(saved.getLocation().getIdLoc());
            response.setLocationName(saved.getLocation().getName());
        }

        return response;

    }

    @Override
    public void deleteClient(Long id) {

        Client client = clientRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Client not found"));

        clientRepository.delete(client);

    }


}
