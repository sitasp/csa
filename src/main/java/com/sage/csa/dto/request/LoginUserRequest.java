package com.sage.csa.dto.request;

public record LoginUserRequest(
        String userName,
        String password
) {
}
