package com.example.limboadduser.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super("User with email " + message + " already exists.");
    }

}
