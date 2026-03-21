package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.DTO.SessionDTO;
import com.projecttuto.vehicule_rental.services.UserLoginSessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.util.List;


@RestController
@RequestMapping("/sessions")
@CrossOrigin("*")
public class UserLoginSessionController {

    private final UserLoginSessionService service;

    public UserLoginSessionController(UserLoginSessionService service) {
        this.service = service;
    }

    @PostMapping("/")
    public ResponseEntity<Void> save(HttpServletRequest request) {
        service.saveSession(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/")
    public ResponseEntity<List<SessionDTO>> findAllUserLoginSessions(){
        List<SessionDTO> sessions = service.findAllUserLoginSessions();
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/by-date")
    public ResponseEntity<List<SessionDTO>> findAllUserLoginSessionsByLoginDate(
            @RequestParam Instant date,
            @RequestParam String id){
        List<SessionDTO> sessions = service.findAllUserLoginSessionsByLoginDate(date,id);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/by-email")
    public ResponseEntity<List<SessionDTO>> findAllUserLoginSessionsByEmail(
            @RequestParam String email){
        List<SessionDTO> sessions = service.findAllUserLoginSessionsByEmail(email);
        return ResponseEntity.ok(sessions);
    }

    @GetMapping("/list/sessions")
    public ResponseEntity<Page<SessionDTO>> findAllUserLoginSessionsByEmailPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam String email){

        Page<SessionDTO> sessionList = service.findAllUseLoginSessionsByEmailPage(email,page,size);

        if (sessionList != null) {
            return ResponseEntity.ok(sessionList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> findSessionById(@PathVariable String id){
        SessionDTO sessionDTO = service.getSession(id);
        return ResponseEntity.ok(sessionDTO);
    }

}