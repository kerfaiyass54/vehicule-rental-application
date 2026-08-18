package com.projecttuto.vehicule_rental.controllers;


import com.projecttuto.vehicule_rental.dto.SessionDTO;
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
    public ResponseEntity<Page<SessionDTO>> findAllUserLoginSessionsByEmailPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam("email") String email) {

        Page<SessionDTO> sessionList =
                service.findAllUseLoginSessionsByEmailPage(email, page, size);

        if (sessionList != null) {
            return ResponseEntity.ok(sessionList);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SessionDTO> findSessionById(
            @PathVariable("id") String id) {

        SessionDTO sessionDTO = service.getSession(id);
        return ResponseEntity.ok(sessionDTO);
    }
}