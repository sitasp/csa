package com.sage.csa.dto.response;

public record ChatSessionResponse(
        String chatId,
        String title,
        String createdAt
) {
}
