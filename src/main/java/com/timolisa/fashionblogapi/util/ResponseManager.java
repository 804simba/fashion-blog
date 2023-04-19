package com.timolisa.fashionblogapi.util;

import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResponseManager<T> {
    public ApiResponse<T> success(T data) {
        return new ApiResponse<>("Request successful", true, data);
    }
    public ApiResponse<String> success(String message) {
        return new ApiResponse<>(message);
    }

    public ApiResponse<Page<Post>> success(Page<Post> data) {
        return new ApiResponse<>("Request successful", true, data);
    }
    public ApiResponse<List<Comment>> success(List<Comment> data) {
        return new ApiResponse<>("Request successful", true, data);
    }

    public ApiResponse<T> error(String usernameAlreadyExists, boolean status) {
        return new ApiResponse<>(usernameAlreadyExists, status);
    }

    public ApiResponse<String> error(String message) {
        return new ApiResponse<>(message);
    }

    public ApiResponse<Page<T>> notFound(String message) {
        return new ApiResponse<>(message);
    }
}
