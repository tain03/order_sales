package com.example.order_sales.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class BaseExceptionHandler {
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestExeption(BadRequestException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
