package com.apartment.app.auth.exception;

public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException() {
        super("Tên đăng nhập hoặc mật khẩu không đúng");
    }
}