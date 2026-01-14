package com.example.restaurant.exception;

public class DuplicateResourceException extends RuntimeException {
    
    public DuplicateResourceException(String message) {
        super(message);
    }
}

