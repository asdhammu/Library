package com.library.dto;

public class ResponseDTO {
    String message;
    public ResponseDTO(String msg){
        this.message = msg;
    }
    public String getMessage() {
        return message;
    }

}
