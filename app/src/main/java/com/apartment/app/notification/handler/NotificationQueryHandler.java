package com.apartment.app.notification.handler;

import com.apartment.app.notification.dto.NotificationResponse;
import com.apartment.domain.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
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

    @Transactional
    public void markRead(UUID id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
    }

    @Transactional
    public void markAllRead() {
        notificationRepository.markAllRead();
    }
}
