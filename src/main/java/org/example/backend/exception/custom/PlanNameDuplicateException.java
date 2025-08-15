package org.example.backend.exception.custom;

public class PlanNameDuplicateException extends RuntimeException {
    public PlanNameDuplicateException(String message) {
        super(message);
    }
}
