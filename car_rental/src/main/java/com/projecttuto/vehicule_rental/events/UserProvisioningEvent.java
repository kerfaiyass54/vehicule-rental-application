package com.projecttuto.vehicule_rental.events;

public record UserProvisioningEvent(
        String username, String firstName, String lastName,
        String email, String password, String role) {}
