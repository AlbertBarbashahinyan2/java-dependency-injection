package org.example.infrastructure.exception;

public class InvalidPostConstructException extends RuntimeException {

    public InvalidPostConstructException(String message) {
        super(message);
    }
}
