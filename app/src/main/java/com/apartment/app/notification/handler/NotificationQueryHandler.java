package com.apartment.app.notification.handler;

import com.apartment.app.notification.dto.NotificationResponse;
import com.apartment.domain.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryHandler {

    private final NotificationRepository notificationRepository;

    public List<NotificationResponse> getLatest() {
        return notificationRepository
                .findAllByOrderByCreatedAtDesc(PageRequest.of(0, 20))
                .stream()
                .map(NotificationResponse::from)
                .toList();
    }

    public Page<NotificationResponse> getPage(String type, boolean unreadOnly, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size,
                org.springframework.data.domain.Sort.by(org.springframework.data.domain.Sort.Direction.DESC, "createdAt"));
        boolean hasType = type != null && !type.isBlank();
        Page<com.apartment.domain.notification.Notification> result;
        if (hasType && unreadOnly) {
            result = notificationRepository.findByTypeAndReadFalseOrderByCreatedAtDesc(type, pageable);
        } else if (hasType) {
            result = notificationRepository.findByTypeOrderByCreatedAtDesc(type, pageable);
        } else if (unreadOnly) {
            result = notificationRepository.findByReadFalseOrderByCreatedAtDesc(pageable);
        } else {
            result = notificationRepository.findAll(pageable);
        }
        return result.map(NotificationResponse::from);
    }

    @Transactional
    public void markRead(UUID id) {
        notificationRepository.markReadById(id);
    }

    @Transactional
    public void markAllRead() {
        notificationRepository.markAllRead();
    }
}
