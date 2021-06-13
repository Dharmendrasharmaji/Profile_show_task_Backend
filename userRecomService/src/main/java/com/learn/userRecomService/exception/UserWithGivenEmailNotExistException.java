package com.learn.userRecomService.exception;

public class UserWithGivenEmailNotExistException extends RuntimeException{
    public UserWithGivenEmailNotExistException(String message) {
        super(message);
    }
}
