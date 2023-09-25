package com.library.error;

public class NoSuchBookException extends RuntimeException {
    public NoSuchBookException(String message){
        super(message);
    }
}
