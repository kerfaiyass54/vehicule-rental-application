package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.ClientAdminDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ClientManagementService {
    Page<ClientAdminDTO> getClients(int page, int size);

    ClientAdminDTO getClient(Long id);

    ClientAdminDTO updateClient(Long id, ClientAdminDTO dto);

    void deleteClient(Long id);

    List<String> getCLientEmails();
}
