package org.example.backend.exception.custom;

public class UploadException extends RuntimeException {
    public UploadException(String message) {
        super(message);
    }
}
