package com.timolisa.fashionblogapi.controller;

import com.timolisa.fashionblogapi.dto.CommentDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fashion-blog/post/comments")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/new/{comment-id}")
    public ResponseEntity<ApiResponse<Comment>> createComment(@PathVariable("comment-id") Long commentId,
                                                              @RequestBody CommentDTO commentDTO)
            throws PostNotFoundException, UnauthorizedAccessException, InvalidInputsException {
        ApiResponse<Comment> response =
                commentService.createComment(commentDTO, commentId);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    @PutMapping("/update-comment/{comment-id}")
    public ResponseEntity<ApiResponse<Comment>> updateComment(@PathVariable("comment-id") Long commentId,
                                                              @RequestBody CommentDTO commentDTO)
            throws PostNotFoundException, UnauthorizedAccessException, InvalidInputsException {
        ApiResponse<Comment> response =
                commentService.updateComment(commentDTO, commentId);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{comment-id}")
    public ResponseEntity<ApiResponse<Comment>> getComment(@PathVariable("comment-id") Long commentId)
            throws PostNotFoundException, UnauthorizedAccessException {
        ApiResponse<Comment> response =
                commentService.findCommentById(commentId);
        return new ResponseEntity<>(response, HttpStatus.FOUND);
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<ApiResponse<List<Comment>>> getAllCommentsForAPost(@PathVariable("post-id") Long postId)
            throws PostNotFoundException, UnauthorizedAccessException {
        ApiResponse<List<Comment>> comments =
                commentService.findAllCommentsForAPost(postId);
        return new ResponseEntity<>(comments, HttpStatus.FOUND);
    }

    @DeleteMapping("/{comment-id}")
    public ResponseEntity<ApiResponse<String>> deleteComment(@PathVariable("comment-id") Long commentId)
            throws PostNotFoundException, UnauthorizedAccessException {
        ApiResponse<String> response =
                commentService.deleteComment(commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
