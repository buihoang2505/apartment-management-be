package com.apartment.interfaces.user;

import com.apartment.app.user.command.*;
import com.apartment.app.user.dto.UserResponse;
import com.apartment.app.user.handler.UserCommandHandler;
import com.apartment.app.user.handler.UserQueryHandler;
import com.apartment.interfaces.shared.response.CommonResponse;
import com.apartment.interfaces.user.request.CreateUserRequest;
import com.apartment.interfaces.user.request.ResetPasswordRequest;
import com.apartment.interfaces.user.request.UpdateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Users", description = "Quản lý người dùng")
@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCommandHandler userCommandHandler;
    private final UserQueryHandler userQueryHandler;
    private final com.apartment.app.shared.port.FileStoragePort fileStoragePort;

    @GetMapping("/me")
    public ResponseEntity<CommonResponse<UserResponse>> getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(CommonResponse.ok(userQueryHandler.findByUsername(username)));
    }

    @GetMapping
    public ResponseEntity<CommonResponse<List<UserResponse>>> getAll() {
        return ResponseEntity.ok(CommonResponse.ok(userQueryHandler.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommonResponse<UserResponse>> getById(@PathVariable("id") UUID id) {
        return ResponseEntity.ok(CommonResponse.ok(userQueryHandler.findById(id)));
    }

    @PostMapping
    public ResponseEntity<CommonResponse<UserResponse>> create(@Valid @RequestBody CreateUserRequest request) {
        CreateUserCommand cmd = new CreateUserCommand(
                request.username(), request.password(), request.fullName(),
                request.email(), request.phone(), request.role());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CommonResponse.ok("Tạo người dùng thành công", userCommandHandler.handle(cmd)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommonResponse<UserResponse>> update(
            @PathVariable("id") UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        UpdateUserCommand cmd = new UpdateUserCommand(
                id, request.fullName(), request.email(), request.phone(), request.role(), request.active(),
                request.headline(), request.biography(), request.language(), request.website(),
                request.facebook(), request.instagram(), request.linkedin(), request.tiktok(), request.github());
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật người dùng thành công", userCommandHandler.handle(cmd)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommonResponse<Void>> delete(@PathVariable("id") UUID id) {
        userCommandHandler.deleteUser(id);
        return ResponseEntity.ok(CommonResponse.ok("Xóa người dùng thành công", null));
    }

    @Operation(summary = "Upload avatar cho user")
    @PostMapping(value = "/{id}/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CommonResponse<UserResponse>> uploadAvatar(
            @PathVariable("id") UUID id,
            @RequestParam("file") MultipartFile file) {
        
        try {
            UserResponse currentUser = userQueryHandler.findById(id);
            if (currentUser.avatarUrl() != null) {
                fileStoragePort.deleteFile(currentUser.avatarUrl());
            }
        } catch (Exception ignored) {
            // Ngó lơ lỗi nêú user ko tồn tại ở đây, nó sẽ ném lỗi tiếp ở command
        }

        String url = fileStoragePort.uploadFile(file, "avatars");
        return ResponseEntity.ok(CommonResponse.ok("Upload avatar thành công",
                userCommandHandler.updateAvatar(id, url)));
    }

    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<CommonResponse<Void>> resetPassword(
            @PathVariable("id") UUID id,
            @Valid @RequestBody ResetPasswordRequest request) {
        userCommandHandler.handle(new ResetPasswordCommand(id, request.newPassword()));
        return ResponseEntity.ok(CommonResponse.ok("Đặt lại mật khẩu thành công", null));
    }
}