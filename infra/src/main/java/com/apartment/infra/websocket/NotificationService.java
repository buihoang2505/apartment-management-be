package com.apartment.infra.websocket;

import com.apartment.app.notification.dto.NotificationResponse;
import com.apartment.app.shared.port.NotificationPort;
import com.apartment.domain.notification.Notification;
import com.apartment.domain.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService implements NotificationPort {

    private final SimpMessagingTemplate messagingTemplate;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public void push(String title, String message, String type, String targetId) {
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .title(title)
                .message(message)
                .type(type)
                .targetId(targetId)
                .build();
        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/topic/notifications",
                NotificationResponse.from(notification));

        log.debug("[Notification] pushed: type={} title='{}'", type, title);
    }
}
