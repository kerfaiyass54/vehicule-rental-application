package com.projecttuto.vehicule_rental.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedOperationException extends RuntimeException {

    private final HttpStatus status;

    public UnauthorizedOperationException(String message) {
        super(message);
        this.status = HttpStatus.FORBIDDEN;
    }

    public HttpStatus getStatus() {
        return status;
    }
}