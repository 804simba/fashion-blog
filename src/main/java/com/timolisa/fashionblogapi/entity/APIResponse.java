package com.timolisa.fashionblogapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class APIResponse<T> {
    private Object message;
    private boolean success;
    private T payload;
    public APIResponse(String usernameAlreadyExists, boolean status) {
        this.message = usernameAlreadyExists;
        this.success = status;
    }

    public APIResponse(String message) {
        this.message = message;
    }
}
