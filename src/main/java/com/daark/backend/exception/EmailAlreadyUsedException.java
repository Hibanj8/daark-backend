package com.daark.backend.exception;
public class EmailAlreadyUsedException extends RuntimeException {
    public EmailAlreadyUsedException(String message) {
        super(message);
    }

    public EmailAlreadyUsedException() {
        super("Cet email est déjà utilisé.");
    }
}