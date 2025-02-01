package com.sage.csa.dto;

import java.time.Instant;

public record UserChatDTO(
        String chatId,
        String title,
        String createdAt
) {
}
