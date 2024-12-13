package com.example.order_sales.exception;

public class ProductNotFoundException extends RuntimeException{
    public ProductNotFoundException(String msg) {
        super(msg);
    }
}
