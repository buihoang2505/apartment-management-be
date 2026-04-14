package com.apartment.app.user.command;

import java.util.UUID;

public record ResetPasswordCommand(UUID userId, String newPassword) {}