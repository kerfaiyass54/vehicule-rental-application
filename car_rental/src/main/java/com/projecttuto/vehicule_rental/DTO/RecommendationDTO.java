package com.projecttuto.vehicule_rental.DTO;

public class RecommendationDTO {

    private Long id;

    private Long vehicleId;

    private String recommendationsJson;

    public RecommendationDTO() {
    }

    public RecommendationDTO(
            Long id,
            Long vehicleId,
            String recommendationsJson
    ) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.recommendationsJson = recommendationsJson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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