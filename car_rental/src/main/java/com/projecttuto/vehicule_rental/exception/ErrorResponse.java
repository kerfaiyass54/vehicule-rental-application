package com.projecttuto.vehicule_rental.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class ErrorResponse {

    private final Instant timestamp;

    private final int status;

    private final String error;

    private final String message;

    private final String path;
}