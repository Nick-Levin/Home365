package com.example.home365.exception;

public class AirlineNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Airline Not Found";
    }

}
