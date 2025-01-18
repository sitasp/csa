package com.sage.csa.dto.response;

import com.sage.csa.dto.UserChatDTO;

import java.util.List;

public record LoginUserResponse(
        String jwtToken,
        List<UserChatDTO> userChats
) {
}
