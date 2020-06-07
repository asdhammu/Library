package com.library.error;

public class NoSuchBookLoanException extends RuntimeException {
    public NoSuchBookLoanException(String msg) {
        super(msg);
    }
}
