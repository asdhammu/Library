package com.library.config;


import com.library.error.BookNotAvailableException;
import com.library.error.BorrowerExistsException;
import com.library.error.BorrowerThresholdException;
import com.library.error.NoSuchBorrowerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BorrowerExistsException.class)
    public final ResponseEntity<ErrorDetail> handleBorrowerExistsException(BorrowerExistsException ex, WebRequest request) {
        ErrorDetail errorDetails = new ErrorDetail(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NoSuchBorrowerException.class)
    public final ResponseEntity<ErrorDetail> handleNoSuchBorrowerException(NoSuchBorrowerException ex, WebRequest request) {
        ErrorDetail errorDetails = new ErrorDetail(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BorrowerThresholdException.class)
    public final ResponseEntity<ErrorDetail> handleBorrowerThresholdException(BorrowerThresholdException ex, WebRequest request) {
        ErrorDetail errorDetails = new ErrorDetail(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BookNotAvailableException.class)
    public final ResponseEntity<ErrorDetail> handleBookNotAvailableException(BookNotAvailableException ex, WebRequest request) {
        ErrorDetail errorDetails = new ErrorDetail(new Date(), ex.getMessage(),
                request.getDescription(false));
        return new ResponseEntity<>(errorDetails, HttpStatus.FORBIDDEN);
    }
}

class ErrorDetail {
    private Date timestamp;
    private String message;
    private String details;

    public ErrorDetail(Date timestamp, String message, String details) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
