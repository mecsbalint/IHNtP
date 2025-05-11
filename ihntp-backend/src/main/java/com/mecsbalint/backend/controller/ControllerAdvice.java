package com.mecsbalint.backend.controller;

import com.mecsbalint.backend.exception.GameNotFoundException;
import com.mecsbalint.backend.exception.IllegalRequestParameterException;
import com.mecsbalint.backend.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(GameNotFoundException.class)
    public String gameNotFoundExceptionHandler(GameNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(UserNotFoundException.class)
    public String userNotFoundExceptionHandler(UserNotFoundException exception) {
        return exception.getMessage();
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalRequestParameterException.class)
    public String illegalRequestParameterExceptionHandler(IllegalRequestParameterException exception) {
        return exception.getMessage();
    }
}
