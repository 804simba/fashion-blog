package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Like;
import com.timolisa.fashionblogapi.entity.Post;

public interface LikeService {
    APIResponse<Like> createLikeForComment(Comment comment);
    APIResponse<String> deleteLikeForComment(Long commentId);
    APIResponse<Like> createLikeForPost(Post post);
    APIResponse<String> deleteLikeForPost(Long postId);
}
