package com.projecttuto.vehicule_rental.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRecommendation {

    @Field(type = FieldType.Text, name = "car_name")
    private String carName;

    @Field(type = FieldType.Keyword)
    private String brand;

    @Field(type = FieldType.Double)
    private Double price;

    @Field(type = FieldType.Double)
    private Double horsepower;

    @Field(type = FieldType.Double, name = "top_speed")
    private Double topSpeed;

    @Field(type = FieldType.Double, name = "acceleration_0_100")
    private Double acceleration0100;

    @Field(type = FieldType.Keyword, name = "fuel_type")
    private String fuelType;

    @Field(type = FieldType.Double)
    private Double torque;

}