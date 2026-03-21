package com.projecttuto.vehicule_rental.entities;

import com.projecttuto.vehicule_rental.enums.FileType;
import com.projecttuto.vehicule_rental.enums.Transmission;
import com.projecttuto.vehicule_rental.enums.VehiculeStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import java.time.Instant;


@Getter
@Setter
@NoArgsConstructor
@Document(indexName = "files_vehicule")
public class FileVehicule {

    @Id
    private String id;
    private String fileName;
    private String supplierName;
    private FileType fileType;
    private String content;
    @Field(type = FieldType.Date)
    private Instant uploadDate;
    private String vehiculeName;
    private String color;
    private String brand;
    private double price;
    private int highSpeed;
    private Transmission transmission;
    private VehiculeStatus vehiculeStatus;

}
