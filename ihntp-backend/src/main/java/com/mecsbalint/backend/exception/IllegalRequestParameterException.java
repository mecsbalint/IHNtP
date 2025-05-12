package com.mecsbalint.backend.exception;

public class IllegalRequestParameterException extends RuntimeException {
    public IllegalRequestParameterException(String requestParam) {
        super(String.format("Illegal request parameter: %s", requestParam));
    }
}
