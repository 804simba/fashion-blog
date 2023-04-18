package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Like;

public interface LikeService {
    ApiResponse<Like> createLikeForComment(Comment comment);
    ApiResponse<String> deleteLikeForComment(Long commentId);
}
