package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.dto.request.PostDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.exception.UserDoesNotExistException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;


public interface PostService {
    APIResponse<Post> createPost(PostDTO postDto) throws UnauthorizedAccessException, InvalidInputsException, UserDoesNotExistException;
    APIResponse<Post> findPostById(Long postId) throws PostNotFoundException, UnauthorizedAccessException;
    APIResponse<Page<Post>> findAllPosts(Pageable pageable) throws PostNotFoundException, UnauthorizedAccessException;
    APIResponse<List<Post>> searchForPosts(Specification<Post> specification);
    APIResponse<Post> updatePost(Long postId, PostDTO postDTO) throws UnauthorizedAccessException, PostNotFoundException;
    APIResponse<String> deletePost(Long postId) throws UnauthorizedAccessException, PostNotFoundException;
}
