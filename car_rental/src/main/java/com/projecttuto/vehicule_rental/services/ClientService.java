package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.ClientDTO;
import com.projecttuto.vehicule_rental.dto.ClientDashboardDTO;


public interface ClientService {

    ClientDashboardDTO getDashboard(String clientEmail);

    ClientDTO getClient(String clientEmail);



}
