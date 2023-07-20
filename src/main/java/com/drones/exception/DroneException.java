package com.drones.exception;

import org.springframework.http.HttpStatus;

public class DroneException extends Exception {
    private final HttpStatus status;
    private final String message;

    public DroneException(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    @Override
    public String getMessage() {
        return message;
    }
}

