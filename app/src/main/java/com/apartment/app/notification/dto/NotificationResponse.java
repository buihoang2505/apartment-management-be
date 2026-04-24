package com.apartment.app.notification.dto;

import com.apartment.domain.notification.Notification;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        String title,
        String message,
        String type,
        String targetId,
        boolean read,
        LocalDateTime createdAt
) {
    public static NotificationResponse from(Notification n) {
        return new NotificationResponse(
                n.getId(), n.getTitle(), n.getMessage(),
                n.getType(), n.getTargetId(), n.isRead(), n.getCreatedAt()
        );
    }
}
