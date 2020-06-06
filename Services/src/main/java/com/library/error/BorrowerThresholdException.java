package com.library.error;

public class BorrowerThresholdException extends RuntimeException{
    public BorrowerThresholdException(String message){
        super(message);
    }
}
