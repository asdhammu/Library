package com.library.error;

public class NoSuchBorrowerException extends RuntimeException{
    public NoSuchBorrowerException(String message){
        super(message);
    }
}
