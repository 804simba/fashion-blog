package com.timolisa.fashionblogapi.service;

import com.timolisa.fashionblogapi.dto.request.CommentDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.exception.UserDoesNotExistException;

import java.util.List;

public interface CommentService {
    APIResponse<Comment> createComment(CommentDTO commentDTO, Long postId) throws UnauthorizedAccessException, InvalidInputsException, PostNotFoundException, UserDoesNotExistException;
    void saveComment(Long postId, Comment comment);
    APIResponse<Comment> findCommentById(Long id) throws PostNotFoundException, UnauthorizedAccessException;
    APIResponse<List<Comment>> findAllCommentsForAPost(Long postId) throws UnauthorizedAccessException, PostNotFoundException;
    APIResponse<Comment> updateComment(CommentDTO commentDTO, Long commentId) throws UnauthorizedAccessException, PostNotFoundException, InvalidInputsException;
    APIResponse<String> deleteComment(Long commentId) throws UnauthorizedAccessException, PostNotFoundException;
}
