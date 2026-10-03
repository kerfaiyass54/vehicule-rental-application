package com.projecttuto.account.model;

public record UserProvisioningEvent(
        String username, String firstName, String lastName,
        String email, String password, String role) {}
