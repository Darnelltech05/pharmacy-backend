package com.samedconnect.pharmacy_backend.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> {
    private boolean success;
    private String message;
    private T data;
    private Object errors;
    private long timestamp;

    public static <T> Response<T> success(T data) {
        return Response.<T>builder()
                .success(true)
                .message("Operation successful")
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> Response<T> success(String message, T data) {
        return Response.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> Response<T> error(String message) {
        return Response.<T>builder()
                .success(false)
                .message(message)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> Response<T> error(String message, Object errors) {
        return Response.<T>builder()
                .success(false)
                .message(message)
                .errors(errors)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}