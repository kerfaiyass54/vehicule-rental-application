package com.projecttuto.vehicule_rental.mappers;

import com.projecttuto.vehicule_rental.DTO.AdressDTO;
import com.projecttuto.vehicule_rental.entities.Adress;
import org.springframework.stereotype.Component;


@Component
public class AdressDTOMapper {

    public AdressDTO mapToAdress(Adress adress) {
        AdressDTO adressDTO = new AdressDTO();
        adressDTO.setIdAdress(adress.getIdAdress());
        adressDTO.setRoad(adress.getRoad());
        adressDTO.setNumber(adress.getNumber());
        return adressDTO;
    }
}
