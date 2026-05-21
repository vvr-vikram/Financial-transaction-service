package com.example.finance.exception;

public class IdempotentRequestException extends RuntimeException {
    public IdempotentRequestException(String message) {
        super(message);
    }
}
