package com.apartment.interfaces.shared.response;

public record CommonResponse<T>(
        boolean success,
        String message,
        T data
) {
    public static <T> CommonResponse<T> ok(T data) {
        return new CommonResponse<>(true, "Success", data);
    }

    public static <T> CommonResponse<T> ok(String message, T data) {
        return new CommonResponse<>(true, message, data);
    }

    public static CommonResponse<Void> error(String message) {
        return new CommonResponse<>(false, message, null);
    }
}