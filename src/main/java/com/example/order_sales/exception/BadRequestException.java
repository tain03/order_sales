package com.example.order_sales.exception;

public class BadRequestException extends RuntimeException {
    public BadRequestException(String msg) {
        super(msg);
    }
}
