package com.mecsbalint.backend.exception;

public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String searchType, String searchValue) {
        super(String.format("Game not found with this %s: %s", searchType, searchValue));
    }
}
