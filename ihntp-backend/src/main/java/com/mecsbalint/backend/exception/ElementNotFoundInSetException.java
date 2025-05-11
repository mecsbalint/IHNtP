package com.mecsbalint.backend.exception;

public class ElementNotFoundInSetException extends RuntimeException {
    public ElementNotFoundInSetException(String element) {
        super(String.format("%s element not found in the Set", element));
    }
}
