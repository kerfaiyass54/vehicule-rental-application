package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.BuyingDTO;
import com.projecttuto.vehicule_rental.entities.Address;
import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Location;
import com.projecttuto.vehicule_rental.entities.Supplier;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.AddressStatus;
import com.projecttuto.vehicule_rental.repositories.AddressRepository;
import com.projecttuto.vehicule_rental.enums.BuyStatus;
import com.projecttuto.vehicule_rental.repositories.BuyingRepository;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.BuyingService;
import com.projecttuto.vehicule_rental.services.LocationEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class BuyingServiceImpl implements BuyingService {

    private final BuyingRepository buyingRepository;
    private final VehiculeRepository vehiculeRepository;
    private final ClientRepository clientRepository;
    private final AddressRepository addressRepository;
    private final LocationEventPublisher locationEventPublisher;


    // -------------------------------------------------------------------------
    // ADD BUYING
    // -------------------------------------------------------------------------

    @Override
    public Buying addBuying(
            Long vehiculeId,
            String clientEmail,
            Integer period,
            boolean renew
    ) {

        Vehicule vehicule = findVehiculeById(vehiculeId);

        Client client = findClientByEmail(clientEmail);
        Buying buying = createBuying(
                vehicule,
                client,
                period,
                renew
        );

        Buying savedBuying = saveBuying(buying);
        locationEventPublisher.publish("buying.created", "buying", savedBuying.getIdBuying(), null,
                supplierLocation(savedBuying.getVehiculeSupplier(), client.getLocation()));
        return savedBuying;
    }


    // -------------------------------------------------------------------------
    // GET BUYINGS BY CLIENT
    // -------------------------------------------------------------------------

    @Override
    public Page<BuyingDTO> getBuyingByClient(
            String clientEmail,
            int page,
            int size
    ) {

        Client client = findClientByEmail(clientEmail);

        Pageable pageable = PageRequest.of(page, size);

        return buyingRepository
                .findByClient(client, pageable)
                .map(this::mapToDTO);
    }


    // -------------------------------------------------------------------------
    // VEHICLE
    // -------------------------------------------------------------------------

    private Vehicule findVehiculeById(Long vehiculeId) {

        return vehiculeRepository
                .findById(vehiculeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Vehicule not found with id: " + vehiculeId
                        )
                );
    }


    // -------------------------------------------------------------------------
    // CLIENT
    // -------------------------------------------------------------------------

    private Client findClientByEmail(String clientEmail) {

        Client client =
                clientRepository.findClientByEmail(clientEmail);

        if (client == null) {
            throw new RuntimeException(
                    "Client not found with email: " + clientEmail
            );
        }

        return client;
    }


    // -------------------------------------------------------------------------
    // BUYING
    // -------------------------------------------------------------------------

    private Buying createBuying(
            Vehicule vehicule,
            Client client,
            Integer period,
            boolean renew
    ) {

        Buying buying = new Buying();

        buying.setDateBuy(Instant.now());

        buying.setPeriodBuy(period);

        buying.setBuyStatus(BuyStatus.BEING_USED);

        buying.setRenew(renew);

        buying.setVehicle(vehicule);

        buying.setClient(client);

        // Supplier comes directly from the vehicle
        buying.setVehiculeSupplier(
                vehicule.getSupplier()
        );

        return buying;
    }


    private Buying saveBuying(Buying buying) {

        return buyingRepository.save(buying);
    }

    private Location supplierLocation(Supplier supplier, Location clientLocation) {
        if (supplier != null) {
            Location location = addressRepository.findAddressesBySupplier(supplier).stream()
                    .filter(address -> address.getAddressStatus() == AddressStatus.ASSIGNED)
                    .map(Address::getLocation)
                    .findFirst()
                    .orElse(null);
            if (location != null) {
                return location;
            }
        }
        return clientLocation;
    }


    // -------------------------------------------------------------------------
    // DTO MAPPING
    // -------------------------------------------------------------------------

    private BuyingDTO mapToDTO(Buying buying) {

        BuyingDTO dto = new BuyingDTO();

        dto.setIdBuying(buying.getIdBuying());
        dto.setDateBuy(buying.getDateBuy());
        dto.setPeriodBuy(buying.getPeriodBuy());
        dto.setBuyStatus(buying.getBuyStatus());
        dto.setRenew(buying.isRenew());

        if (buying.getVehicle() != null) {

            dto.setVehiculeName(
                    buying.getVehicle().getVehicleName()
            );
        }

        if (buying.getVehiculeSupplier() != null) {

            dto.setSupplierName(
                    buying.getVehiculeSupplier().getSupplierName()
            );
        }

        return dto;
    }
}
