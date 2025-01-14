package com.sage.csa.utils;

import com.sage.csa.dto.ApiResponse;

public class ControllerUtils {

    public static <T> ApiResponse<T> apiResponse(int status, ApiResponse.StatusCode statusCode, String message, T data) {
        return new ApiResponse<T>(status, statusCode, message, data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<T>(200, ApiResponse.StatusCode.OK, "success", data);
    }

    public static <T> ApiResponse<T> five00(String message) {
        return new ApiResponse<T>(500, ApiResponse.StatusCode.INTERNAL_SERVER_ERROR, message, null);
    }

    public static <T> ApiResponse<T> four00(String message) {
        return new ApiResponse<T>(400, ApiResponse.StatusCode.BAD_REQUEST, message, null);
    }
}
