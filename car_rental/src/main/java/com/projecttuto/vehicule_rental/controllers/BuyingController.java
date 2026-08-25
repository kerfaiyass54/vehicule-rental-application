package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.BuyingDTO;
import com.projecttuto.vehicule_rental.entities.Buying;
import com.projecttuto.vehicule_rental.services.BuyingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/buyings")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(
        name = "Buying Management",
        description = "APIs for vehicle buying management"
)
public class BuyingController {

    private final BuyingService buyingService;


    // -------------------------------------------------------------------------
    // CREATE BUYING
    // -------------------------------------------------------------------------

    @Operation(
            summary = "Create a vehicle buying",
            description = """
                    Creates a new vehicle buying for a client.
                    The buying date and status are generated automatically.
                    The supplier is retrieved from the selected vehicle.
                    """
    )
    @PostMapping
    public ResponseEntity<Buying> addBuying(

            @RequestParam
            @NotNull(message = "Vehicle ID is required")
            Long vehiculeId,

            @RequestParam
            @NotBlank(message = "Client email is required")
            @Email(message = "Invalid client email")
            String clientEmail,

            @RequestParam
            @Min(
                    value = 1,
                    message = "Period must be at least 1"
            )
            Integer period,

            @RequestParam
            boolean renew
    ) {

        log.info(
                "Creating buying: vehicleId={}, clientEmail={}, period={}, renew={}",
                vehiculeId,
                clientEmail,
                period,
                renew
        );

        Buying buying = buyingService.addBuying(
                vehiculeId,
                clientEmail,
                period,
                renew
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(buying);
    }


    // -------------------------------------------------------------------------
    // GET BUYINGS BY CLIENT
    // -------------------------------------------------------------------------

    @Operation(
            summary = "Get buyings for a client",
            description = "Returns the paginated list of vehicle buyings belonging to a client."
    )
    @GetMapping("/clients/{clientEmail}")
    public ResponseEntity<Page<BuyingDTO>> getBuyingByClient(

            @PathVariable
            @Email(message = "Invalid client email")
            String clientEmail,

            @RequestParam(defaultValue = "0")
            @Min(
                    value = 0,
                    message = "Page must be greater than or equal to 0"
            )
            int page,

            @RequestParam(defaultValue = "10")
            @Min(
                    value = 1,
                    message = "Size must be greater than 0"
            )
            int size
    ) {

        log.info(
                "Fetching buyings for client email: {}, page: {}, size: {}",
                clientEmail,
                page,
                size
        );

        Page<BuyingDTO> buyings =
                buyingService.getBuyingByClient(
                        clientEmail,
                        page,
                        size
                );

        return ResponseEntity.ok(buyings);
    }
}