package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Like;
import com.timolisa.fashionblogapi.entity.Post;

public interface LikeService {
    ApiResponse<Like> createLikeForComment(Comment comment);
    ApiResponse<String> deleteLikeForComment(Long commentId);
    ApiResponse<Like> createLikeForPost(Post post);
    ApiResponse<String> deleteLikeForPost(Long postId);
}
