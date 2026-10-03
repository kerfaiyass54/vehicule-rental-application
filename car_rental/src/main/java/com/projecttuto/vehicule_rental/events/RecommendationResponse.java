package com.projecttuto.vehicule_rental.events;

import java.time.Instant;
import java.util.List;

public record RecommendationResponse(
        String email,
        String locationName,
        Double budget,
        List<SupplierRecommendation> suppliers,
        Instant generatedAt
) {
    public record SupplierRecommendation(
            Long supplierId,
            String supplierName,
            String road,
            Integer addressNumber,
            List<VehicleRecommendation> vehicles
    ) {}

    public record VehicleRecommendation(
            Long vehicleId,
            String vehicleName,
            String brand,
            Double price,
            Integer maxSpeed,
            String transmission,
            String status,
            Double score,
            String reason
    ) {}
}
