package org.example.infrastructure.exception;

public class QualifierNotFoundException extends RuntimeException {

    public QualifierNotFoundException(String message) {
        super(message);
    }
}
