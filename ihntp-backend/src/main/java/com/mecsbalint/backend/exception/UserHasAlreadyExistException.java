package com.mecsbalint.backend.exception;

public class UserHasAlreadyExistException extends RuntimeException {
    public UserHasAlreadyExistException(String userEmail) {
        super(String.format("There is already a User with this e-amil: %s", userEmail));
    }
}
