package com.apartment.app.user.handler;

import com.apartment.app.user.command.*;
import com.apartment.app.user.dto.UserResponse;
import com.apartment.app.user.exception.UserNotFoundException;
import com.apartment.domain.user.User;
import com.apartment.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class UserCommandHandler {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse handle(CreateUserCommand cmd) {
        if (userRepository.existsByUsername(cmd.username())) {
            throw new IllegalArgumentException("Username '" + cmd.username() + "' đã tồn tại");
        }
        User user = User.builder()
                .username(cmd.username())
                .password(passwordEncoder.encode(cmd.password()))
                .fullName(cmd.fullName())
                .email(cmd.email())
                .phone(cmd.phone())
                .role(cmd.role() != null ? cmd.role() : "MANAGER")
                .active(true)
                .build();
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse handle(UpdateUserCommand cmd) {
        User user = userRepository.findById(cmd.id())
                .orElseThrow(() -> new UserNotFoundException(cmd.id()));
        user.setFullName(cmd.fullName());
        user.setEmail(cmd.email());
        user.setPhone(cmd.phone());
        if (cmd.role() != null) user.setRole(cmd.role());
        user.setActive(cmd.active());
        return UserResponse.from(userRepository.save(user));
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user.getUsername().equals(currentUsername)) {
            throw new IllegalArgumentException("Không thể xóa tài khoản đang đăng nhập");
        }
        userRepository.deleteById(id);
    }

    public void handle(ResetPasswordCommand cmd) {
        User user = userRepository.findById(cmd.userId())
                .orElseThrow(() -> new UserNotFoundException(cmd.userId()));
        user.setPassword(passwordEncoder.encode(cmd.newPassword()));
        userRepository.save(user);
    }
}