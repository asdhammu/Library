package com.library.exception;

public class BorrowerThresholdException extends RuntimeException{
    public BorrowerThresholdException(String message){
        super(message);
    }
}
