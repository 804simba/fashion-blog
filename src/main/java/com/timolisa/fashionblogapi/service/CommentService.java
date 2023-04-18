package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.dto.CommentDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;

import java.util.List;

public interface CommentService {
    ApiResponse<Comment> createComment(CommentDTO commentDTO, Long postId) throws UnauthorizedAccessException, InvalidInputsException;

    ApiResponse<Comment> findCommentById(Long id) throws PostNotFoundException, UnauthorizedAccessException;
    ApiResponse<List<Comment>> findAllCommentsForAPost(Long postId) throws UnauthorizedAccessException;
    ApiResponse<Comment> updateComment(CommentDTO commentDTO, Long commentId) throws UnauthorizedAccessException;
    ApiResponse<String> deleteComment(Long commentId) throws UnauthorizedAccessException;
}
