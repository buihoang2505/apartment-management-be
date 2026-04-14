package com.apartment.app.user.exception;

import java.util.UUID;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(UUID id) {
        super("Người dùng không tồn tại: " + id);
    }
}