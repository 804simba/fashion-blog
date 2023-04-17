package com.timolisa.fashionblogapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private Object message;
    private boolean success;
    private T data;

    public ApiResponse(String usernameAlreadyExists, boolean status) {
        this.message = usernameAlreadyExists;
        this.success = status;
    }

    public ApiResponse(String message) {
        this.message = message;
    }
}
