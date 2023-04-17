package com.timolisa.fashionblogapi.controller;

import com.timolisa.fashionblogapi.dto.PostDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.service.PostService;
import com.timolisa.fashionblogapi.util.ResponseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fashion-blog/posts")
public class PostController {
    private final PostService postService;
    private final ResponseManager<Post> responseManager;

    @PostMapping("/new")
    public ResponseEntity<ApiResponse<Post>> createPost(@RequestBody PostDTO postDTO)
            throws UnauthorizedAccessException, InvalidInputsException {
        ApiResponse<Post> post = postService.createPost(postDTO);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Post>>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            ApiResponse<Page<Post>> response = postService.findAllPosts(pageable);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } catch (PostNotFoundException | UnauthorizedAccessException e) {
            ApiResponse<Page<Post>> response = responseManager.notFound(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<ApiResponse<Post>> viewPost(@PathVariable("post-id") Long postId)
            throws PostNotFoundException, UnauthorizedAccessException {
        ApiResponse<Post> foundPost = postService.findPostById(postId);
        return new ResponseEntity<>(foundPost, HttpStatus.FOUND);
    }

    @PostMapping("/update/{post-id}")
    public ResponseEntity<ApiResponse<Post>> editPost(@RequestBody PostDTO postDTO,
                                                      @PathVariable("post-id") Long postId)
            throws PostNotFoundException, UnauthorizedAccessException {
        ApiResponse<Post> existingPost = postService.updatePostById(postId, postDTO);
        return new ResponseEntity<>(existingPost, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{post-id}")
    public ResponseEntity<String> deletePost(@PathVariable("post-id") Long postId)
            throws PostNotFoundException, UnauthorizedAccessException {
        postService.deletePostById(postId);
        return new ResponseEntity<>("Deleted successfully", HttpStatus.OK);
    }
}
