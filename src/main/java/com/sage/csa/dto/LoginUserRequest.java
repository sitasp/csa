package com.sage.csa.dto;

public record LoginUserRequest(
        String userName,
        String password
) {
}
