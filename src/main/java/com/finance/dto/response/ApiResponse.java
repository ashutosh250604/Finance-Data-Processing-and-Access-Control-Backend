package com.finance.dto.response;

/**
 * Generic API Response wrapper
 * 
 * This provides a consistent response structure for all API endpoints:
 * {
 * "success": true/false,
 * "message": "Some message",
 * "data": { actual data object }
 * }
 * 
 * Using generics <T> allows this to wrap any type of data.
 */
public class ApiResponse<T> {

    private boolean success;
    private String message;
    private T data;

    // Constructor for success with data
    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }

    // Constructor for success without data
    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    // Static factory methods for convenience
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(true, message);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message);
    }

    public static <T> ApiResponse<T> error(String message, T data) {
        return new ApiResponse<>(false, message, data);
    }

    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
