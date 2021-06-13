package com.learn.useraccount.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<?> alreadyExists(UserAlreadyExistsException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> alreadyExists(UserNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
