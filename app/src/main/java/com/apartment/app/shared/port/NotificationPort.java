package com.apartment.app.shared.port;

public interface NotificationPort {
    void push(String title, String message, String type, String targetId);
}
