package com.mecsbalint.backend.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userEmail) {
        super(String.format("There is no User with this e-amil: %s", userEmail));
    }
}
