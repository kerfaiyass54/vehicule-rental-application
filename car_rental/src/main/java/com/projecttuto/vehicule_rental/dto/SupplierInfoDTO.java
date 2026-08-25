package com.projecttuto.vehicule_rental.dto;


import lombok.Data;

@Data
public class SupplierInfoDTO {
    private Long idSupp;
    private String suppName;
    private String nationality;
    private String email;
    private Integer numberVehicules;
    private Integer numberSubscriptions;
    private Integer numberBuyings;
}
