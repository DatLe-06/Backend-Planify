package org.example.backend.exception.custom;

public class PriorityNotFoundException extends RuntimeException {
    public PriorityNotFoundException(String message) {
        super(message);
    }
}
