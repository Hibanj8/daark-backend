package com.daark.backend.exception;

public class BadCredentialsException extends RuntimeException {
    public BadCredentialsException() {
        super("Email ou mot de passe incorrect");
    }
}
