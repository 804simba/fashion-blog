package com.timolisa.fashionblogapi.controller;

import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Like;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.service.CommentService;
import com.timolisa.fashionblogapi.service.LikeService;
import com.timolisa.fashionblogapi.util.ResponseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fashion-blog/comment/likes")
public class LikeController {
    private final LikeService likeService;
    private final CommentService commentService;

    @PostMapping("/{comment-id}")
    public ResponseEntity<ApiResponse<Like>> createLikeForComment(@PathVariable("comment-id") Long commentId)
            throws PostNotFoundException, UnauthorizedAccessException {
        Comment comment = commentService
                .findCommentById(commentId).getData();

        ApiResponse<Like> response = likeService.createLikeForComment(comment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<ApiResponse<String>> unlikeAComment(@PathVariable("comment-id") Long commentId) {
        ApiResponse<String> response =
                likeService.deleteLikeForComment(commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
