package com.projecttuto.vehicule_rental.services;

import com.projecttuto.vehicule_rental.dto.SessionDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;

public interface UserLoginSessionService {

    public void saveSession(HttpServletRequest request);


    public Page<SessionDTO> findAllUseLoginSessionsByEmailPage(String email, int page, int size);

    public SessionDTO getSession(String id);
}
