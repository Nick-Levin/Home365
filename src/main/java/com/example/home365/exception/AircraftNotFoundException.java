package com.example.home365.exception;

public class AircraftNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Aircraft not found";
    }
}
