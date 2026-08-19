package com.projecttuto.vehicule_rental.exception;

import org.springframework.http.HttpStatus;

public class ResourceAlreadyExistsException extends RuntimeException {

    private final HttpStatus status;

    public ResourceAlreadyExistsException(String message) {
        super(message);
        this.status = HttpStatus.CONFLICT;
    }

    public HttpStatus getStatus() {
        return status;
    }
}