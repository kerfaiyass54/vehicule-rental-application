package com.projecttuto.vehicule_rental.servicesImpl;

import com.projecttuto.vehicule_rental.DTO.UserDTO;
import com.projecttuto.vehicule_rental.entities.Client;
import com.projecttuto.vehicule_rental.entities.Repair;
import com.projecttuto.vehicule_rental.entities.Supplier;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.projecttuto.vehicule_rental.repositories.ClientRepository;
import com.projecttuto.vehicule_rental.repositories.RepairRepository;
import com.projecttuto.vehicule_rental.repositories.SupplierRepository;
import com.projecttuto.vehicule_rental.services.AuthenticationService;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private RepairRepository   repairRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Override
    public void signUpUser(UserDTO user){
        
    }

    @Override
    public boolean isEmailExist(String email){
        return getAllMails().contains(email);
    }

    public List<String> getAllMails(){
        List<Supplier> suppliers = supplierRepository.findAll();
        List<Client> clients = clientRepository.findAll();
        List<Repair> repairs = repairRepository.findAll();
        List<String> mails = new ArrayList<String>();
        for(Supplier s : suppliers){
            mails.add(s.getEmail());
        }
        for(Client c : clients){
            mails.add(c.getEmail());
        }
        for(Repair r : repairs){
            mails.add(r.getEmail());
        }

        return mails;
    }


}
