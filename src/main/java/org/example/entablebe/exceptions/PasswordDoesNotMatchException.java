package org.example.entablebe.exceptions;

public class PasswordDoesNotMatchException extends RuntimeException {
    private String message;

    public PasswordDoesNotMatchException(String message) {
        super(message);
    }
}
