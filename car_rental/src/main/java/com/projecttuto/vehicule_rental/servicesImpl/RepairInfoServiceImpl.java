package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.DTO.RepairDTO;
import com.projecttuto.vehicule_rental.DTO.RepairInfoDTO;
import com.projecttuto.vehicule_rental.DTO.VehiculeDTO;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.entities.Vehicule;
import com.projecttuto.vehicule_rental.enums.RepairStatus;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.repositories.RepairInfoRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.RepairInfoService;

import java.time.Instant;
import java.time.Instant;

@Service
@AllArgsConstructor
@Slf4j
public class RepairInfoServiceImpl implements RepairInfoService {

    @Autowired
    private RepairInfoRepository repairInfoRepository;

    @Autowired
    private VehiculeRepository vehiculeRepository;

    @Autowired
    private RepairRepository repairRepository;

    @Override
    public void updateDateStart(Instant date, RepairInfo repairInfo){
        RepairInfo repairInfo1 = repairInfoRepository.getById(repairInfo.getIdInfo());
        repairInfo1.setDateStart(date);
        repairInfoRepository.save(repairInfo1);
    }

    @Override
    public void updateStatus(RepairInfo repairInfo, RepairStatus repairStatus){
        RepairInfo repairInfo1 = repairInfoRepository.getById(repairInfo.getIdInfo());
        repairInfo1.setRepairStatus(repairStatus);
        repairInfoRepository.save(repairInfo1);
    }

    @Override
    public RepairInfoDTO getRepairInfoById(long id){
        RepairInfo repairInfo = repairInfoRepository.getById(id);
        RepairInfoDTO repairInfoDTO = new RepairInfoDTO();
        repairInfoDTO.setIdInfo(repairInfo.getIdInfo());
        repairInfoDTO.setDateStart(repairInfo.getDateStart());
        repairInfoDTO.setDateStart(repairInfo.getDateStart());
        return repairInfoDTO;
    }

    @Override
    public VehiculeDTO getVehiculeById(String name){
        VehiculeDTO vehiculeDTO = new VehiculeDTO();
        Vehicule vehicule = vehiculeRepository.findVehiculeByNameVehicule(name);
        vehiculeDTO.setIdVehicule(vehicule.getIdVehicule());
        vehiculeDTO.setNameVehicule(vehicule.getNameVehicule());
        vehiculeDTO.setVehiculeStatus(vehicule.getVehiculeStatus());
        vehiculeDTO.setBrand(vehicule.getBrand());
        vehiculeDTO.setColor(vehicule.getColor());
        vehiculeDTO.setHighSpeed(vehicule.getHighSpeed());
        vehiculeDTO.setTransmission(vehicule.getTransmission());
        vehiculeDTO.setPrice(vehicule.getPrice());
        return vehiculeDTO;
    }

    @Override
    public RepairDTO getRepair(String name){
        RepairDTO repairDTO = new RepairDTO();
        Repair repair = repairRepository.findRepairByNameRepair(name);
        repairDTO.setIdRepair(repair.getIdRepair());
        repairDTO.setNameRepair(repair.getNameRepair());
        return repairDTO;
    }

    @Override
    public void addReparation(String vehiculeName, String repairName, Instant date){
        RepairInfo repairInfo = new RepairInfo();
        Vehicule vehicule = vehiculeRepository.findVehiculeByNameVehicule(vehiculeName);
        Repair repair = repairRepository.findRepairByNameRepair(repairName);
        repairInfo.setDateStart(date);
        repairInfo.setVehicule(vehicule);
        repairInfo.setRepair(repair);
        if(Instant.now().equals(repairInfo.getDateStart())){
            repairInfo.setRepairStatus(RepairStatus.PENDING_FINISH);
        }
        else{
            repairInfo.setRepairStatus(RepairStatus.PENDING_START);
        }
        repairInfoRepository.save(repairInfo);
    }

}
