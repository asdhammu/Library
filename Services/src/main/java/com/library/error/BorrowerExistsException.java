package com.library.error;

public class BorrowerExistsException extends RuntimeException {
    public BorrowerExistsException(String msg) {
        super(msg);
    }
}
