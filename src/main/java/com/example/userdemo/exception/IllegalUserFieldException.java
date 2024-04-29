package com.example.userdemo.exception;

public class IllegalUserFieldException extends RuntimeException{

    public IllegalUserFieldException(final String message) {
        super(message);
    }
}

