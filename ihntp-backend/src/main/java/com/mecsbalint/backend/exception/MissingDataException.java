package com.mecsbalint.backend.exception;

public class MissingDataException extends RuntimeException {
    public MissingDataException(String element, String entity) {
        super(String.format("[%s] element has missing required data for %s entity", element, entity));
    }
}
