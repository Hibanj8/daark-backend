package com.daark.backend.exception;

public class UsernameAlreadyUsedException extends RuntimeException {
    public UsernameAlreadyUsedException(String message) {
        super(message);
    }
    public UsernameAlreadyUsedException() {
        super("Ce nom d'utilisateur est déjà utilisé.");
    }

}
