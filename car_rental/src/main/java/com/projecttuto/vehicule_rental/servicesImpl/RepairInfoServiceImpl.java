package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.dto.RepairInfoDTO;
import com.projecttuto.vehicule_rental.entities.RepairInfo;
import com.projecttuto.vehicule_rental.exception.ResourceNotFoundException;
import com.projecttuto.vehicule_rental.repositories.RepairInfoRepository;
import com.projecttuto.vehicule_rental.services.RepairInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class RepairInfoServiceImpl implements RepairInfoService {

    private final RepairInfoRepository repairInfoRepository;


    @Override
    public RepairInfoDTO getRepairInfoById(Long id) {

        log.info("Fetching repair info with id: {}", id);

        RepairInfo repairInfo = findRepairInfoById(id);

        return mapToDTO(repairInfo);
    }


    /*
     * Find repair information by ID.
     */
    private RepairInfo findRepairInfoById(Long id) {

        return repairInfoRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Repair information not found with id: " + id
                        )
                );
    }


    /*
     * Convert entity to DTO.
     */
    private RepairInfoDTO mapToDTO(RepairInfo repairInfo) {

        RepairInfoDTO dto = new RepairInfoDTO();

        dto.setIdRepairInfo(repairInfo.getIdRepairInfo());
        dto.setDateStart(repairInfo.getDateStart());
        dto.setDateStart(repairInfo.getDateStart());

        return dto;
    }
}