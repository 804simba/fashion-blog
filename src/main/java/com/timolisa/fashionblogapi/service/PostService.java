package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.dto.PostDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PostService {
    ApiResponse<Post> createPost(PostDTO postDto) throws UnauthorizedAccessException, InvalidInputsException;
    ApiResponse<Post> findPostById(Long postId) throws PostNotFoundException, UnauthorizedAccessException;
    ApiResponse<Page<Post>> findAllPosts(Pageable pageable) throws PostNotFoundException, UnauthorizedAccessException;
    ApiResponse<Post> updatePost(Long postId, PostDTO postDTO) throws UnauthorizedAccessException, PostNotFoundException;
    ApiResponse<String> deletePost(Long postId) throws UnauthorizedAccessException, PostNotFoundException;
}
