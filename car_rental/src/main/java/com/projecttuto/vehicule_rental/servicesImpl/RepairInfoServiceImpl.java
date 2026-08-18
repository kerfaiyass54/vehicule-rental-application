package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.RepairDTO;
import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.repositories.RepairInfoRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.VehiculeRepository;
import com.projecttuto.vehicule_rental.services.RepairInfoService;

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
    public RepairInfoDTO getRepairInfoById(Long id){
        RepairInfo repairInfo = repairInfoRepository.getById(id);
        RepairInfoDTO repairInfoDTO = new RepairInfoDTO();
        repairInfoDTO.setIdRepairInfo(repairInfo.getIdInfo());
        repairInfoDTO.setDateStart(repairInfo.getDateStart());
        repairInfoDTO.setDateStart(repairInfo.getDateStart());
        return repairInfoDTO;
    }


    @Override
    public RepairDTO getRepair(String name){
        RepairDTO repairDTO = new RepairDTO();
        Repair repair = repairRepository.findRepairByNameRepair(name);
        repairDTO.setIdRepair(repair.getIdRepair());
        repairDTO.setNameRepair(repair.getNameRepair());
        return repairDTO;
    }



}
