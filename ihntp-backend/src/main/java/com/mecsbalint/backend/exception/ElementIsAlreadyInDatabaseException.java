package com.mecsbalint.backend.exception;

public class ElementIsAlreadyInDatabaseException extends RuntimeException {
    public ElementIsAlreadyInDatabaseException(String element, String entity) {
        super(String.format("[%s] element is already in the %s entity's table", element, entity));
    }
}
