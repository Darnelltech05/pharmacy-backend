package com.samedconnect.pharmacy_backend.exception;

public class InvalidStockOperationException extends RuntimeException {

    public InvalidStockOperationException(String message) {
        super(message);
    }
}
