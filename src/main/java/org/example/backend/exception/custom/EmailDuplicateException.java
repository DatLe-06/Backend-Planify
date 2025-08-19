package org.example.backend.exception.custom;

import jakarta.persistence.EntityExistsException;

public class EmailDuplicateException extends EntityExistsException {
    public EmailDuplicateException(String message) {
        super(message);
    }
}
