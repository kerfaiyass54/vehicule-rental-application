package com.projecttuto.vehicule_rental.DTO;

public class RecommendationDTO {

    private Long vehicleId;

    private String recommendationsJson;

    public RecommendationDTO() {
    }

    public RecommendationDTO(
            Long vehicleId,
            String recommendationsJson
    ) {
        this.vehicleId = vehicleId;
        this.recommendationsJson = recommendationsJson;
    }

    public Long getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Long vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getRecommendationsJson() {
        return recommendationsJson;
    }

    public void setRecommendationsJson(
            String recommendationsJson
    ) {
        this.recommendationsJson = recommendationsJson;
    }
}