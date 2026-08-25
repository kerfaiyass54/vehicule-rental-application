package com.projecttuto.vehicule_rental.controllers;

import com.projecttuto.vehicule_rental.dto.SupplierInfoDTO;
import com.projecttuto.vehicule_rental.dto.VehiculeSupplierDTO;
import com.projecttuto.vehicule_rental.services.ClientSupplierService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suppliers")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Supplier",
        description = "APIs for searching and retrieving supplier information"
)
public class ClientSupplierController {

    private final ClientSupplierService supplierService;


    // ---------------------------------------------------------
    // SEARCH SUPPLIERS
    // ---------------------------------------------------------

    @GetMapping("/search")
    @Operation(
            summary = "Search suppliers",
            description = """
                    Searches suppliers using an optional keyword.
                    The keyword can match the supplier name, nationality,
                    or email address.

                    Results are paginated.
                    """
    )
    public ResponseEntity<Page<SupplierInfoDTO>> searchSuppliers(

            @Parameter(
                    description = "Search keyword. Searches by supplier name, nationality or email.",
                    example = "Germany"
            )
            @RequestParam(
                    required = false,
                    defaultValue = ""
            )
            String keyword,

            @Parameter(
                    description = "Page number, starting from 0.",
                    example = "0"
            )
            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @Parameter(
                    description = "Number of suppliers per page.",
                    example = "10"
            )
            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ) {

        log.info(
                "Searching suppliers with keyword='{}', page={}, size={}",
                keyword,
                page,
                size
        );

        Page<SupplierInfoDTO> suppliers =
                supplierService.searchSuppliers(
                        keyword,
                        page,
                        size
                );

        return ResponseEntity.ok(suppliers);
    }


    // ---------------------------------------------------------
    // GET ALL SUPPLIERS
    // ---------------------------------------------------------

    @GetMapping
    @Operation(
            summary = "Get all suppliers",
            description = "Returns all suppliers using pagination."
    )
    public ResponseEntity<Page<SupplierInfoDTO>> getAllSuppliers(

            @Parameter(
                    description = "Page number, starting from 0.",
                    example = "0"
            )
            @RequestParam(
                    defaultValue = "0"
            )
            int page,

            @Parameter(
                    description = "Number of suppliers per page.",
                    example = "10"
            )
            @RequestParam(
                    defaultValue = "10"
            )
            int size
    ) {

        log.info(
                "Fetching suppliers, page={}, size={}",
                page,
                size
        );

        Page<SupplierInfoDTO> suppliers =
                supplierService.searchSuppliers(
                        page,
                        size
                );

        return ResponseEntity.ok(suppliers);
    }


    @GetMapping("/supplier/{supplierId}/available")
    @Operation(
            summary = "Get available vehicles of a supplier",
            description = "Returns all available vehicles belonging to the specified supplier."
    )
    public ResponseEntity<Page<VehiculeSupplierDTO>> getAvailableVehiculesBySupplier(

            @Parameter(
                    description = "Supplier ID",
                    example = "1"
            )
            @PathVariable Long supplierId,

            @Parameter(
                    description = "Page number starting from 0",
                    example = "0"
            )
            @RequestParam(defaultValue = "0") int page,

            @Parameter(
                    description = "Number of vehicles per page",
                    example = "10"
            )
            @RequestParam(defaultValue = "10") int size
    ) {

        log.info(
                "Fetching available vehicles for supplierId={}, page={}, size={}",
                supplierId,
                page,
                size
        );

        Page<VehiculeSupplierDTO> vehicles =
                supplierService.getAvailableVehiculesBySupplier(
                        supplierId,
                        page,
                        size
                );

        return ResponseEntity.ok(vehicles);
    }
}