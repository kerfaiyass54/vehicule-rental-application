package com.projecttuto.vehicule_rental.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "recommendations")
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long vehicleId;

    @Column(columnDefinition = "LONGTEXT")
    private String recommendationsJson;

    public Recommendation() {
    }

    public Recommendation(
            Long vehicleId,
            String recommendationsJson
    ) {
        this.vehicleId = vehicleId;
        this.recommendationsJson = recommendationsJson;
    }

    public Long getId() {
        return id;
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