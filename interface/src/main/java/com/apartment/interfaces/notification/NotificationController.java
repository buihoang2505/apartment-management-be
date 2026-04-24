package com.apartment.interfaces.notification;

import com.apartment.app.notification.dto.NotificationResponse;
import com.apartment.app.notification.handler.NotificationQueryHandler;
import com.apartment.interfaces.shared.response.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Notifications", description = "Quản lý thông báo")
@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationQueryHandler notificationQueryHandler;

    @Operation(summary = "Lấy 20 thông báo mới nhất")
    @GetMapping
    public ResponseEntity<CommonResponse<List<NotificationResponse>>> getAll() {
        return ResponseEntity.ok(CommonResponse.ok(notificationQueryHandler.getLatest()));
    }

    @Operation(summary = "Đánh dấu một thông báo đã đọc")
    @PutMapping("/{id}/read")
    public ResponseEntity<CommonResponse<Void>> markRead(@PathVariable("id") UUID id) {
        notificationQueryHandler.markRead(id);
        return ResponseEntity.ok(CommonResponse.ok("Đã đánh dấu đọc", null));
    }

    @Operation(summary = "Đánh dấu tất cả thông báo đã đọc")
    @PutMapping("/read-all")
    public ResponseEntity<CommonResponse<Void>> markAllRead() {
        notificationQueryHandler.markAllRead();
        return ResponseEntity.ok(CommonResponse.ok("Đã đánh dấu tất cả đã đọc", null));
    }
}
