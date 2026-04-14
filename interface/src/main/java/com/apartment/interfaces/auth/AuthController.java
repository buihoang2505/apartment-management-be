package com.apartment.interfaces.auth;

import com.apartment.app.auth.command.LoginCommand;
import com.apartment.app.auth.dto.TokenResponse;
import com.apartment.app.auth.handler.AuthCommandHandler;
import com.apartment.interfaces.auth.request.LoginRequest;
import com.apartment.interfaces.shared.response.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthCommandHandler authCommandHandler;

    @PostMapping("/login")
    public ResponseEntity<CommonResponse<TokenResponse>> login(@Valid @RequestBody LoginRequest request) {
        LoginCommand cmd = new LoginCommand(request.username(), request.password());
        return ResponseEntity.ok(CommonResponse.ok("Đăng nhập thành công", authCommandHandler.handle(cmd)));
    }
}