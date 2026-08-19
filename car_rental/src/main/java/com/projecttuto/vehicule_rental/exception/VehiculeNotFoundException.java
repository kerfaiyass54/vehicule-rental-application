package com.projecttuto.vehicule_rental.exception;

public class VehiculeNotFoundException extends RuntimeException {

    public VehiculeNotFoundException(String message) {
        super(message);
    }
}