package com.sage.csa.dto;

public record CreateUserRequest(
        String userName,
        String password,
        String mobileNumber
) {
}
