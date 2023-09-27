package com.library.exception;

public class NoSuchBookLoanException extends RuntimeException {
    public NoSuchBookLoanException(String msg) {
        super(msg);
    }
}
