package com.example.restaurant.exception;

public class RestaurantClosedException extends RuntimeException {
    
    public RestaurantClosedException(String message) {
        super(message);
    }
}

