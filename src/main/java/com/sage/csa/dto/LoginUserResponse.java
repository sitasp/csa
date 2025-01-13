package com.sage.csa.dto;

import java.util.List;

public record LoginUserResponse(
        String jwtToken,
        List<UserChatDTO> userChats
) {
}
