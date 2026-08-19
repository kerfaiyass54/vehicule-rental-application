package com.projecttuto.vehicule_rental.servicesImpl;


import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.repositories.RepairInfoRepository;
import com.projecttuto.vehicule_rental.services.RepairInfoService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class RepairInfoServiceImpl implements RepairInfoService {

    @Autowired
    private RepairInfoRepository repairInfoRepository;


    @Override
    public RepairInfoDTO getRepairInfoById(Long id){
        RepairInfo repairInfo = repairInfoRepository.getById(id);
        RepairInfoDTO repairInfoDTO = new RepairInfoDTO();
        repairInfoDTO.setIdRepairInfo(repairInfo.getIdRepairInfo());
        repairInfoDTO.setDateStart(repairInfo.getDateStart());
        repairInfoDTO.setDateStart(repairInfo.getDateStart());
        return repairInfoDTO;
    }





}
