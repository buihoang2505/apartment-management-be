package com.apartment.interfaces.user;

import com.apartment.app.user.command.UpdateProfileCommand;
import com.apartment.app.user.dto.UserResponse;
import com.apartment.app.user.handler.UserCommandHandler;
import com.apartment.app.user.handler.UserQueryHandler;
import com.apartment.interfaces.shared.response.CommonResponse;
import com.apartment.interfaces.user.request.UpdateProfileRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User Profile", description = "Người dùng tự quản lý thông tin cá nhân")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserCommandHandler userCommandHandler;
    private final UserQueryHandler userQueryHandler;

    @Operation(summary = "Lấy thông tin người dùng hiện tại")
    @GetMapping("/me")
    public ResponseEntity<CommonResponse<UserResponse>> getMe() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ResponseEntity.ok(CommonResponse.ok(userQueryHandler.findByUsername(username)));
    }

    @Operation(summary = "Cập nhật thông tin cá nhân")
    @PutMapping("/me/profile")
    public ResponseEntity<CommonResponse<UserResponse>> updateProfile(
            @RequestBody UpdateProfileRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserResponse current = userQueryHandler.findByUsername(username);
        UpdateProfileCommand cmd = new UpdateProfileCommand(
                current.id(),
                request.fullName(),
                request.headline(),
                request.biography(),
                request.language(),
                request.website(),
                request.facebook(),
                request.instagram(),
                request.linkedin(),
                request.tiktok(),
                request.github()
        );
        return ResponseEntity.ok(CommonResponse.ok("Cập nhật thông tin thành công", userCommandHandler.handleProfile(cmd)));
    }
}
