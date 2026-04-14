package com.apartment.app.auth;

public interface TokenProvider {
    String generateToken(String username);
    long getExpiration();
}