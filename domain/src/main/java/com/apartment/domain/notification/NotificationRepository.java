package com.apartment.domain.notification;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findAllByOrderByCreatedAtDesc(Pageable pageable);

    Page<Notification> findByTypeOrderByCreatedAtDesc(String type, Pageable pageable);

    Page<Notification> findByReadFalseOrderByCreatedAtDesc(Pageable pageable);

    Page<Notification> findByTypeAndReadFalseOrderByCreatedAtDesc(String type, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Notification n SET n.read = true WHERE n.read = false")
    int markAllRead();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Notification n SET n.read = true WHERE n.id = :id AND n.read = false")
    int markReadById(@org.springframework.data.repository.query.Param("id") UUID id);
}
