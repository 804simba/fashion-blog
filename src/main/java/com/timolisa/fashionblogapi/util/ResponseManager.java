package com.timolisa.fashionblogapi.util;

import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class ResponseManager<T> {
    public ApiResponse<T> success(T data) {
        return new ApiResponse<>("Request successful", true, data);
    }

    public ApiResponse<Page<Post>> success(Page<Post> data) {
        return new ApiResponse<>("Request successful", true, data);
    }

    public ApiResponse<T> error(String usernameAlreadyExists, boolean status) {
        return new ApiResponse<>(usernameAlreadyExists, status);
    }

    public ApiResponse<Page<T>> notFound(String message) {
        return new ApiResponse<>(message);
    }
}
