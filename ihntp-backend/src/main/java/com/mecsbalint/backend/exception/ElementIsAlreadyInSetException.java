package com.mecsbalint.backend.exception;

public class ElementIsAlreadyInSetException extends RuntimeException {
    public ElementIsAlreadyInSetException(String element) {
        super(String.format("%s element is already in the Set", element));
    }
}
