package com.Ecommerce_backend.exception;

public class OurException extends RuntimeException{
    private int code;
    private String message;

    public OurException(String message) {
        super(message);
    }

    public OurException(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
