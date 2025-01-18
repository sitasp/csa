package com.sage.csa.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse <T> {
    private int status;
    private StatusCode statusCode;
    private String message;
    private T data;

    public static enum StatusCode {
        OK,
        BAD_REQUEST,
        INTERNAL_SERVER_ERROR
    }
}
