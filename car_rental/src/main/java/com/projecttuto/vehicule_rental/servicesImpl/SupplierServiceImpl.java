package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.*;
import com.projecttuto.vehicule_rental.entities.*;
import com.projecttuto.vehicule_rental.enums.*;
import com.projecttuto.vehicule_rental.repositories.*;
import com.projecttuto.vehicule_rental.services.SupplierService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SupplierServiceImpl implements SupplierService {

    private final SupplierRepository supplierRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final VehiculeRepository vehiculeRepository;
    private final AddressRepository adressRepository;
    private final DemandRepository demandRepository;
    private final LocationRepository locationRepository;
    private final BuyingRepository buyingRepository;
    private final TicketRepository ticketRepository;

    @Override
    public DemandResponseDTO refuseDemand(Long demandId) {

        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() ->
                        new RuntimeException("Demand not found"));

        demand.setStatusConfirm(ConfirmStatus.REFUSED);

        Ticket ticket = demand.getTicket();

        ticket.setStatus(RepairDemandStatus.REJECTED);

        ticketRepository.save(ticket);

        Demand saved = demandRepository.save(demand);

        DemandResponseDTO dto = new DemandResponseDTO();

        dto.setIdDemand(saved.getIdDemand());

        dto.setType(saved.getType());

        dto.setDateAsk(saved.getDateAsk());

        dto.setEstimatedTime(saved.getEstimatedTime());

        dto.setStatus(saved.getStatusConfirm());

        dto.setVehiculeName(saved.getVehicule().getNameVehicule());

        dto.setRepairName(saved.getTicket().getRepair().getNameRepair());

        Buying buying =
                buyingRepository.findBuyingByVehicule(saved.getVehicule());

        if (buying != null)
            dto.setClientName(buying.getClient().getNameClient());

        dto.setTicketId(saved.getTicket().getIdTicket());

        return dto;

    }

    @Override
    public SupplierDashboardDTO getDashboard(String supplierEmail) {

        Supplier supplier = supplierRepository.findSupplierByEmail(supplierEmail);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        SupplierDashboardDTO dto = new SupplierDashboardDTO();

        dto.setSupplierName(supplier.getSuppName());

        dto.setTotalVehicles(
                (int) vehiculeRepository.countBySupplier(supplier));

        dto.setTotalBuyings(
                (int) buyingRepository.countByVehiculeSupplier(supplier));

        dto.setActiveBuyings(
                (int) buyingRepository.countByVehiculeSupplierAndBuyStatus(
                        supplier,
                        BuyStatus.BEING_USED));

        dto.setTotalSubscriptions(
                (int) subscriptionRepository.countBySupplier(supplier));

        dto.setTotalDemands(
                (int) demandRepository.countBySupplier(supplier));

        dto.setApprovedDemands(
                (int) demandRepository.countBySupplierAndStatusConfirm(
                        supplier,
                        ConfirmStatus.APPROVED));

        dto.setRefusedDemands(
                (int) demandRepository.countBySupplierAndStatusConfirm(
                        supplier,
                        ConfirmStatus.REFUSED));

        dto.setPendingDemands(
                (int) demandRepository.countBySupplierAndStatusConfirm(
                        supplier,
                        ConfirmStatus.PENDING));

        return dto;
    }

    @Override
    public Page<DemandResponseDTO> checkDemands(
            String supplierEmail,
            int page,
            int size) {

        Supplier supplier = supplierRepository.findSupplierByEmail(supplierEmail);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return demandRepository.findBySupplier(supplier, pageable)
                .map(demand -> {

                    DemandResponseDTO dto = new DemandResponseDTO();

                    dto.setIdDemand(demand.getIdDemand());

                    dto.setType(demand.getType());

                    dto.setDateAsk(demand.getDateAsk());

                    dto.setEstimatedTime(demand.getEstimatedTime());

                    dto.setStatus(demand.getStatusConfirm());

                    dto.setVehiculeName(
                            demand.getVehicule().getNameVehicule());

                    dto.setRepairName(
                            demand.getTicket().getRepair().getNameRepair());

                    Buying buying =
                            buyingRepository.findBuyingByVehicule(
                                    demand.getVehicule());

                    if (buying != null) {
                        dto.setClientName(
                                buying.getClient().getNameClient());
                    }

                    dto.setTicketId(
                            demand.getTicket().getIdTicket());

                    return dto;

                });

    }


    @Override
    public DemandResponseDTO approveDemand(Long demandId) {

        Demand demand = demandRepository.findById(demandId)
                .orElseThrow(() ->
                        new RuntimeException("Demand not found"));

        demand.setStatusConfirm(ConfirmStatus.APPROVED);

        Ticket ticket = demand.getTicket();

        ticket.setStatus(RepairDemandStatus.ACCEPTED);

        ticketRepository.save(ticket);

        Demand saved = demandRepository.save(demand);

        DemandResponseDTO dto = new DemandResponseDTO();

        dto.setIdDemand(saved.getIdDemand());

        dto.setType(saved.getType());

        dto.setDateAsk(saved.getDateAsk());

        dto.setEstimatedTime(saved.getEstimatedTime());

        dto.setStatus(saved.getStatusConfirm());

        dto.setVehiculeName(saved.getVehicule().getNameVehicule());

        dto.setRepairName(saved.getTicket().getRepair().getNameRepair());

        Buying buying =
                buyingRepository.findBuyingByVehicule(saved.getVehicule());

        if (buying != null)
            dto.setClientName(buying.getClient().getNameClient());

        dto.setTicketId(saved.getTicket().getIdTicket());

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
        dto.setSuppName(supplier.getSuppName());

        return dto;
    }



    @Override
    public SupplierDTO getSupplier(String supplierName) {

        Supplier supplier = supplierRepository.findBySuppName(supplierName)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));

        SupplierDTO dto = new SupplierDTO();

        dto.setIdSupp(supplier.getIdSupp());
        dto.setNationality(supplier.getNationality());
        dto.setSuppName(supplier.getSuppName());
        dto.setEmail(supplier.getEmail());

        return dto;
    }






    @Override
    public Integer getSupplierVehicules(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getVehicules()
                .size();
    }




    @Override
    public Page<SubscriptionResponseDTO> checkSubscriptions(
            String supplierEmail,
            int page,
            int size) {

        Supplier supplier = supplierRepository.findSupplierByEmail(supplierEmail);

        if (supplier == null) {
            throw new RuntimeException("Supplier not found");
        }

        Pageable pageable = PageRequest.of(page, size);

        return subscriptionRepository.findBySupplier(supplier, pageable)
                .map(subscription -> {

                    SubscriptionResponseDTO dto = new SubscriptionResponseDTO();

                    dto.setIdSubscription(subscription.getIdSubscrip());

                    dto.setClientName(
                            subscription.getClient().getNameClient());

                    dto.setClientEmail(
                            subscription.getClient().getEmail());

                    dto.setType(subscription.getType());

                    dto.setDateStart(subscription.getDateStart());

                    dto.setPrice(subscription.getPrice());

                    dto.setReduce(subscription.getReduce());

                    return dto;
                });
    }

    @Override
    public List<Long> getVehiculesIds(String email){
        Supplier supplier = supplierRepository.findSupplierByEmail(email);
        return supplier.getVehicules().stream().map(Vehicule::getIdVehicule).toList();
    }


    @Override
    public Integer getSupplierAddresses(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getAddresses()
                .size();
    }


    @Override
    public Integer getSupplierCountries(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        return supplier.getAddresses()
                .stream()
                .map(Address::getLocation)
                .map(Location::getCountry)
                .distinct()
                .toList()
                .size();
    }


    @Override
    public Integer getSupplierLocations(String email) {

        return supplierRepository.findSupplierByEmail(email)
                .getAddresses()
                .stream()
                .map(Address::getLocation)
                .distinct()
                .toList()
                .size();
    }


    @Override
    public List<AddressDTO> getAddressesList(String email, int size, int page) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getAddresses() == null)
            return List.of();

        return supplier.getAddresses()
                .stream()
                .skip((Long) page * size)
                .limit(size)
                .map(adress -> {

                    AddressDTO dto = new AddressDTO();

                    dto.setIdAddress(adress.getIdAddress());
                    dto.setRoad(adress.getRoad());
                    dto.setNumber(adress.getNumber());
                    dto.setLocation(adress.getLocation().getName());
                    dto.setSupplierEmail(adress.getSupplier().getEmail());
                    dto.setAddressStatus(adress.getAddressStatus());

                    return dto;

                }).toList();
    }


    @Override
    public List<String> getCountries(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getAddresses() == null)
            return List.of();

        return supplier.getAddresses()
                .stream()
                .map(Address::getLocation)
                .map(Location::getCountry)
                .distinct()
                .toList();
    }


    @Override
    public List<LocationDTO> getLocations(String email, int size, int page) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getAddresses() == null)
            return List.of();

        return supplier.getAddresses()
                .stream()
                .map(Address::getLocation)
                .distinct()
                .skip((Long) page * size)
                .limit(size)
                .map(location -> {

                    LocationDTO dto = new LocationDTO();

                    dto.setIdLoc(location.getIdLoc());
                    dto.setName(location.getName());
                    dto.setCountry(location.getCountry());
                    dto.setPosition(location.getPosition());

                    return dto;

                }).toList();
    }


    @Override
    public Integer getTotalVehicules(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getVehicules() == null)
            return 0;

        return supplier.getVehicules().size();
    }


    @Override
    public int countBySupplierEmailAndVehiculeStatus(String email, VehiculeStatus status) {

        return (int) supplierRepository.findSupplierByEmail(email)
                .getVehicules()
                .stream()
                .filter(v -> v.getVehiculeStatus() == status)
                .count();
    }


    @Override
    public List<VehiculeDTO> getVehiculesList(String email) {

        Supplier supplier = supplierRepository.findSupplierByEmail(email);

        if (supplier == null || supplier.getVehicules() == null)
            return List.of();

        return supplier.getVehicules()
                .stream()
                .map(vehicule -> {

                    VehiculeDTO dto = new VehiculeDTO();

                    dto.setIdVehicule(vehicule.getIdVehicule());
                    dto.setNameVehicule(vehicule.getNameVehicule());
                    dto.setBrand(vehicule.getBrand());
                    dto.setColor(vehicule.getColor());
                    dto.setPrice(vehicule.getPrice());
                    dto.setHighSpeed(vehicule.getHighSpeed());
                    dto.setTransmission(vehicule.getTransmission());
                    dto.setVehiculeStatus(vehicule.getVehiculeStatus());

                    return dto;

                }).toList();
    }






    @Override
    public Vehicule addVehiculeNew(VehiculeCreation vehiculeCreation, String supplierEmail){
        Vehicule vehicule = new Vehicule();
        vehicule.setNameVehicule(vehiculeCreation.getNameVehicule());
        vehicule.setBrand(vehiculeCreation.getBrand());
        vehicule.setColor(vehiculeCreation.getColor());
        vehicule.setPrice(vehiculeCreation.getPrice());
        vehicule.setHighSpeed(vehiculeCreation.getHighSpeed());
        vehicule.setTransmission(vehiculeCreation.getTransmission());
        vehicule.setVehiculeStatus(VehiculeStatus.AVAILABLE);
        vehicule.setSupplier(supplierRepository.findSupplierByEmail(supplierEmail));
        return vehiculeRepository.save(vehicule);
    }

    @Override
    public Address addAddressNew(AddressCreation addressCreation, String supplierEmail){
        Address adress = new Address();
        adress.setRoad(addressCreation.getRoad());
        adress.setNumber(addressCreation.getNumber());
        adress.setAddressStatus(AddressStatus.ASSIGNED);
        adress.setSupplier(supplierRepository.findSupplierByEmail(supplierEmail));
        adress.setLocation(locationRepository.findLocationByName(addressCreation.getLocation()));
        return adressRepository.save(adress);
    }

    @Override
    public void freeAddress(Long AddressId){
        Optional<Address> adressOptional = adressRepository.findById(AddressId);
        if (adressOptional.isPresent()) {
            Address adress = adressOptional.get();
            adress.setSupplier(null);
            adress.setAddressStatus(AddressStatus.EMPTY);
            adressRepository.save(adress);
        }
    }

    @Override
    public List<String> getVehiculesNames(String email){
        Supplier supplier = supplierRepository.findSupplierByEmail(email);
        return supplier.getVehicules().stream().map(Vehicule::getNameVehicule).toList();
    }


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
                            buying.getVehicule().getNameVehicule());

                    dto.setClientName(
                            buying.getClient().getNameClient());

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