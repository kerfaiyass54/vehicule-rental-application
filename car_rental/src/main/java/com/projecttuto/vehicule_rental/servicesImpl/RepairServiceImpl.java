package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.DTO.*;
import com.projecttuto.vehicule_rental.entities.*;
import com.projecttuto.vehicule_rental.enums.ConfirmStatus;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import com.projecttuto.vehicule_rental.enums.StatusRepair;
import com.projecttuto.vehicule_rental.repositories.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.services.RepairService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairRepository repairRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RepairInfoRepository repairInfoRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private AdminRepository adminRepository;

    private final  BuyingRepository buyingRepository;

    private final DemandRepository demandRepository;

    private final SupplierRepository supplierRepository;

    @Override
    public RepairProfileDTO updateLocation(
            String repairEmail,
            Long locationId) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        Location location = locationRepository.findById(locationId)
                .orElseThrow(() ->
                        new RuntimeException("Location not found"));

        repair.setLocation(location);

        repairRepository.save(repair);

        RepairProfileDTO dto = new RepairProfileDTO();

        dto.setIdRepair(repair.getIdRepair());

        dto.setNameRepair(repair.getNameRepair());

        dto.setEmail(repair.getEmail());

        dto.setRole(repair.getRole());

        dto.setLocationName(location.getName());

        dto.setCountry(location.getCountry());

        dto.setPosition(location.getPosition());

        return dto;
    }

    @Override
    public RepairProfileDTO getInfo(String repairEmail) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        RepairProfileDTO dto = new RepairProfileDTO();

        dto.setIdRepair(repair.getIdRepair());

        dto.setNameRepair(repair.getNameRepair());

        dto.setEmail(repair.getEmail());

        dto.setRole(repair.getRole());

        if (repair.getLocation() != null) {

            dto.setLocationName(repair.getLocation().getName());

            dto.setCountry(repair.getLocation().getCountry());

            dto.setPosition(repair.getLocation().getPosition());

        }

        return dto;
    }

    @Override
    public RepairInfoDTO startRepair(Long ticketId) {

        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (ticket.getStatus() != StatusRepair.ACCEPTED) {
            throw new RuntimeException("Ticket must be accepted first.");
        }

        if (repairInfoRepository.findByVehicule(ticket.getVehicule()) != null) {
            throw new RuntimeException("Vehicle is already under repair.");
        }

        RepairInfo repairInfo = new RepairInfo();

        repairInfo.setVehicule(ticket.getVehicule());

        repairInfo.setRepair(ticket.getRepair());

        repairInfo.setRepairStatus(RepairStatus.PENDING_FINISH);

        RepairInfo saved = repairInfoRepository.save(repairInfo);

        ticket.setStatus(StatusRepair.COMPLETED);

        ticketRepository.save(ticket);

        RepairInfoDTO dto = new RepairInfoDTO();

        dto.setIdRepairInfo(saved.getIdInfo());

        dto.setVehiculeName(saved.getVehicule().getNameVehicule());

        dto.setRepairName(saved.getRepair().getNameRepair());

        dto.setClientName(ticket.getClient().getNameClient());

        dto.setDateStart(saved.getDateStart());

        dto.setRepairStatus(saved.getRepairStatus());

        return dto;
    }

    @Override
    public Page<RepairInfoDTO> checkRepairs(String repairEmail,
                                            int page,
                                            int size) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null)
            throw new RuntimeException("Repair center not found");

        Pageable pageable = PageRequest.of(page, size);

        return repairInfoRepository.findByRepair(repair, pageable)
                .map(info -> {

                    RepairInfoDTO dto = new RepairInfoDTO();

                    dto.setIdRepairInfo(info.getIdInfo());

                    dto.setVehiculeName(info.getVehicule().getNameVehicule());

                    dto.setRepairName(info.getRepair().getNameRepair());

                    Buying buying =
                            buyingRepository.findBuyingByVehicule(info.getVehicule());

                    if (buying != null)
                        dto.setClientName(
                                buying.getClient().getNameClient());

                    dto.setDateStart(info.getDateStart());

                    dto.setRepairStatus(info.getRepairStatus());

                    return dto;
                });

    }


    @Override
    public void cancelRepair(Long repairInfoId) {

        RepairInfo repairInfo = repairInfoRepository.findById(repairInfoId)
                .orElseThrow(() -> new RuntimeException("Repair not found"));

        repairInfo.setRepairStatus(RepairStatus.CANCELLED);

        repairInfoRepository.save(repairInfo);

    }


    @Override
    public Page<RepairTicketDTO> getTickets(
            String repairEmail,
            int page,
            int size) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return ticketRepository.findByRepair(repair, pageable)
                .map(ticket -> {

                    RepairTicketDTO dto = new RepairTicketDTO();

                    dto.setIdTicket(ticket.getIdTicket());

                    dto.setClientName(ticket.getClient().getNameClient());

                    dto.setVehiculeName(ticket.getVehicule().getNameVehicule());

                    dto.setTicketType(ticket.getType());

                    dto.setDescription(ticket.getDecription());

                    dto.setDateTicket(ticket.getDateInsert());

                    dto.setTicketStatus(ticket.getStatus());

                    Demand demand = demandRepository.findDemandByTicket(ticket);

                    if (demand != null) {

                        dto.setDemandType(demand.getType());

                        dto.setEstimatedTime(demand.getEstimatedTime());

                        dto.setSupplierName(
                                demand.getSupplier().getSuppName());

                        dto.setDemandStatus(
                                demand.getStatusConfirm());
                    }

                    return dto;

                });

    }

    @Override
    public RepairTicketDTO createDemand(CreateDemandDTO dto) {

        Repair repair = repairRepository.findRepairByEmail(dto.getRepairEmail());

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        Ticket ticket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found"));

        if (!ticket.getRepair().getIdRepair().equals(repair.getIdRepair())) {
            throw new RuntimeException("This ticket doesn't belong to this repair center.");
        }

        if (demandRepository.findDemandByTicket(ticket) != null) {
            throw new RuntimeException("A demand already exists for this ticket.");
        }

        Supplier supplier = supplierRepository.findSupplierBySuppName(dto.getSupplierName());

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        Demand demand = new Demand();

        demand.setType(dto.getType());

        demand.setEstimatedTime(dto.getEstimatedTime());

        demand.setStatusConfirm(ConfirmStatus.PENDING);

        demand.setTicket(ticket);

        demand.setSupplier(supplier);

        demand.setVehicule(ticket.getVehicule());

        Demand savedDemand = demandRepository.save(demand);

        RepairTicketDTO response = new RepairTicketDTO();

        response.setIdTicket(ticket.getIdTicket());

        response.setClientName(ticket.getClient().getNameClient());

        response.setVehiculeName(ticket.getVehicule().getNameVehicule());

        response.setTicketType(ticket.getType());

        response.setDescription(ticket.getDecription());

        response.setDateTicket(ticket.getDateInsert());

        response.setTicketStatus(ticket.getStatus());

        response.setDemandType(savedDemand.getType());

        response.setEstimatedTime(savedDemand.getEstimatedTime());

        response.setSupplierName(savedDemand.getSupplier().getSuppName());

        response.setDemandStatus(savedDemand.getStatusConfirm());

        return response;
    }

    @Override
    public Page<RepairInfoDTO> getRepairInfos(
            String repairEmail,
            int page,
            int size) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null)
            throw new RuntimeException("Repair center not found");

        Pageable pageable = PageRequest.of(page, size);

        return repairInfoRepository.findByRepair(repair, pageable)
                .map(info -> {

                    RepairInfoDTO dto = new RepairInfoDTO();

                    dto.setIdRepairInfo(info.getIdInfo());

                    dto.setDateStart(info.getDateStart());

                    dto.setRepairStatus(info.getRepairStatus());

                    dto.setVehiculeName(
                            info.getVehicule().getNameVehicule());

                    Buying buying = buyingRepository.findBuyingByVehicule(
                            info.getVehicule());

                    if (buying != null)
                        dto.setClientName(
                                buying.getClient().getNameClient());

                    return dto;
                });
    }

    @Override
    public RepairDashboardDTO getDashboard(String repairEmail) {

        Repair repair = repairRepository.findRepairByEmail(repairEmail);

        if (repair == null) {
            throw new RuntimeException("Repair center not found");
        }

        RepairDashboardDTO dto = new RepairDashboardDTO();

        dto.setRepairName(repair.getNameRepair());

        if (repair.getLocation() != null) {
            dto.setLocation(repair.getLocation().getName());
        }

        // Tickets
        dto.setTotalTickets((int) ticketRepository.countByRepair(repair));

        dto.setPendingTickets((int) ticketRepository.countByRepairAndStatus(
                repair,
                StatusRepair.PENDING));

        dto.setAcceptedTickets((int) ticketRepository.countByRepairAndStatus(
                repair,
                StatusRepair.ACCEPTED));

        dto.setCompletedTickets((int) ticketRepository.countByRepairAndStatus(
                repair,
                StatusRepair.COMPLETED));

        // Repairs
        dto.setActiveRepairs((int) repairInfoRepository.countByRepairAndRepairStatus(
                repair,
                RepairStatus.PENDING_FINISH));

        dto.setCompletedRepairs((int) repairInfoRepository.countByRepairAndRepairStatus(
                repair,
                RepairStatus.FINISHED));

        dto.setCancelledRepairs((int) repairInfoRepository.countByRepairAndRepairStatus(
                repair,
                RepairStatus.CANCELLED));

        // Demands
        dto.setTotalDemands((int) demandRepository.countByTicketRepair(repair));

        dto.setPendingDemands((int) demandRepository.countByTicketRepairAndStatusConfirm(
                repair,
                ConfirmStatus.PENDING));

        dto.setAcceptedDemands((int) demandRepository.countByTicketRepairAndStatusConfirm(
                repair,
                ConfirmStatus.APPROVED));

        dto.setRejectedDemands((int) demandRepository.countByTicketRepairAndStatusConfirm(
                repair,
                ConfirmStatus.REFUSED));

        return dto;
    }



    @Override
    public void addRepair(Repair repair, String location){
        Location loc = locationRepository.findLocationByName(location);
        repair.setLocation(loc);
        repairRepository.save(repair);
    }

    @Override
    public void deleteRepair(long id){
        repairRepository.delete(repairRepository.findById(id).get());
    }
    @Override
    public void updateRepair(RepairDTO repairDTO){
        Repair r = repairRepository.findByNameRepair(repairDTO.getNameRepair()).get();
        r.setNameRepair(repairDTO.getNameRepair());
        r.setPass(repairDTO.getPass());
        r.setEmail(repairDTO.getEmail());
        repairRepository.save(r);
    }

    @Override
    public RepairDTO getRepair(String nameRepair){
        RepairDTO repairDTO = new RepairDTO();
        Repair repair = repairRepository.findRepairByNameRepair(nameRepair);
        repairDTO.setIdRepair(repair.getIdRepair());
        repairDTO.setNameRepair(nameRepair);
        repairDTO.setLocationName(repair.getLocation().getName());
        repairDTO.setEmail(repair.getEmail());
        return repairDTO;
    }

    @Override
    public void changeRepairPassword(Repair repair, String newPassword){
        repairRepository.findByNameRepair(repair.getNameRepair()).get().setPass(newPassword);
    }
    @Override
    public List<Ticket> getTickets(String repairName){
        return repairRepository.findByNameRepair(repairName).get().getTickets();
    }
    @Override
    public List<RepairInfo> getRepairInfo(String repairName){
        return repairRepository.findByNameRepair(repairName).get().getRepairInfos();
    }
    @Override
    public List<Vehicule> getVehicules(String repairName){
        List<RepairInfo> repairInfo = getRepairInfo(repairName);
        List<Vehicule> vehicules = new ArrayList<>();
        for(RepairInfo r : repairInfo){
            vehicules.add(r.getVehicule());
        }
        return vehicules;
    }
    @Override
    public void updateLocation(String repairName, String locationName){
        repairRepository.findByNameRepair(repairName).get().setLocation(locationRepository.findByName(locationName).get());
    }
    @Override
    public LocationDTO getLocation(String locationName){
        LocationDTO locationDTO = new LocationDTO();
        Location location = locationRepository.findByName(locationName).get();
        locationDTO.setIdLoc(location.getIdLoc());
        locationDTO.setCountry(location.getCountry());
        locationDTO.setName(location.getName());
        return locationDTO;
    }


}
