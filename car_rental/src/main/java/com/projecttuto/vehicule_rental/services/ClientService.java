package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.*;
import com.projecttuto.vehicule_rental.entities.Client;
import org.springframework.data.domain.Page;

import java.util.List;


public interface ClientService {

    ClientDashboardDTO getDashboard(String clientEmail);



}
