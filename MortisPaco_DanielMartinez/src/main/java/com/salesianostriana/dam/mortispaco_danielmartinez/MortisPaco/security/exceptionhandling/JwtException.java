package com.salesianostriana.dam.mortispaco_danielmartinez.MortisPaco.security.exceptionhandling;

public class JwtException extends RuntimeException {
    public JwtException(String message) {
        super(message);
    }
}