package com.backuptool.model;

import java.time.LocalDateTime;

public class AppResponse<T> {

    private boolean success;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    private String errorCode;

    private AppResponse() {
        this.timestamp = LocalDateTime.now();
    }

    public static <T> AppResponse<T> success(String message, T data) {
        AppResponse<T> response = new AppResponse<>();
        response.success = true;
        response.message = message;
        response.data = data;
        return response;
    }

    public static <T> AppResponse<T> success(String message) {
        return success(message, null);
    }

    public static <T> AppResponse<T> failure(String message, String errorCode) {
        AppResponse<T> response = new AppResponse<>();
        response.success = false;
        response.message = message;
        response.errorCode = errorCode;
        return response;
    }

    public static <T> AppResponse<T> failure(String message) {
        return failure(message, "GENERAL_ERROR");
    }

    // Getters
    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public T getData() { return data; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getErrorCode() { return errorCode; }

    @Override
    public String toString() {
        return "AppResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", timestamp=" + timestamp +
                ", errorCode='" + errorCode + '\'' +
                '}';
    }
}
