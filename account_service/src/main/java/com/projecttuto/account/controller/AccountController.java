package com.projecttuto.account.controller;

import com.projecttuto.account.model.*;
import com.projecttuto.account.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AccountService service;
    public AccountController(AccountService service) { this.service = service; }
    @GetMapping("/me")
    public UserRepresentation me(@AuthenticationPrincipal Jwt jwt) { return service.current(jwt.getSubject()); }
    @PutMapping("/me")
    public ResponseEntity<Void> update(@AuthenticationPrincipal Jwt jwt, @RequestBody AccountUpdateRequest request) {
        service.update(jwt.getSubject(), request); return ResponseEntity.noContent().build();
    }
    @PutMapping("/me/password")
    public ResponseEntity<Void> password(@AuthenticationPrincipal Jwt jwt, @RequestBody PasswordUpdateRequest request) {
        service.password(jwt.getSubject(), request); return ResponseEntity.noContent().build();
    }
    @PostMapping("/me/logout-all")
    public ResponseEntity<Void> logoutAll(@AuthenticationPrincipal Jwt jwt) {
        service.logoutAll(jwt.getSubject()); return ResponseEntity.noContent().build();
    }
    @PostMapping("/sessions")
    public ResponseEntity<Void> saveSession(@AuthenticationPrincipal Jwt jwt, HttpServletRequest request) {
        service.saveSession(jwt.getSubject(), jwt.getClaimAsString("preferred_username"), jwt.getClaimAsString("email"), request);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/sessions")
    public Page<LoginSessionDocument> sessions(@AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return service.sessions(jwt.getClaimAsString("email"), page, size);
    }
    @PostMapping("/provisioning")
    public ResponseEntity<Void> provisioning(@RequestBody UserProvisioningEvent event) {
        service.provision(event); return ResponseEntity.accepted().build();
    }
}
