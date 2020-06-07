package com.library.dto;

public class Response {
    String message;

    public Response(String msg){
        this.message = msg;
    }

    public String getMessage() {
        return message;
    }

}
