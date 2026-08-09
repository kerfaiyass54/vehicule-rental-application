package com.projecttuto.vehicule_rental.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "vehicle_recommendations")
public class RecommendationDocument {

    @Id
    private String id;

    @Field(type = FieldType.Integer, name = "vehicle_id")
    private Integer vehicleId;

    @Field(type = FieldType.Nested)
    private List<CarRecommendation> recommendations;

}