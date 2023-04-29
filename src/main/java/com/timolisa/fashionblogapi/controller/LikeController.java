package com.timolisa.fashionblogapi.controller;

import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Like;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.service.CommentService;
import com.timolisa.fashionblogapi.service.LikeService;
import com.timolisa.fashionblogapi.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fashion-blog/comment")
public class LikeController {
    private final LikeService likeService;
    private final CommentService commentService;
    private final PostService postService;

    @PostMapping("/like-post/{post-id}")
    public ResponseEntity<APIResponse<Like>> createLikeForPost(@PathVariable("post-id") Long postId)
            throws PostNotFoundException, UnauthorizedAccessException {
        Post post = postService
                .findPostById(postId).getPayload();

        APIResponse<Like> response = likeService.createLikeForPost(post);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/unlike-post/{post-id}")
    public ResponseEntity<APIResponse<String>> unlikeAPost(@PathVariable("post-id") Long postId) {
        APIResponse<String> response =
                likeService.deleteLikeForPost(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/like-comment/{comment-id}")
    public ResponseEntity<APIResponse<Like>> createLikeForComment(@PathVariable("comment-id") Long commentId)
            throws PostNotFoundException, UnauthorizedAccessException {
        Comment comment = commentService
                .findCommentById(commentId).getPayload();

        APIResponse<Like> response = likeService.createLikeForComment(comment);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/unlike-comment/{comment-id}")
    public ResponseEntity<APIResponse<String>> unlikeAComment(@PathVariable("comment-id") Long commentId) {
        APIResponse<String> response =
                likeService.deleteLikeForComment(commentId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
