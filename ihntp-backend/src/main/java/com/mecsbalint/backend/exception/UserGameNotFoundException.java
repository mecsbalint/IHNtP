package com.mecsbalint.backend.exception;

public class UserGameNotFoundException extends RuntimeException {
    public UserGameNotFoundException(String searchType, String searchValue) {
        super(String.format("GameUser entity not found with this %s: %s", searchType, searchValue));
    }
}
