package com.timolisa.fashionblogapi.controller;

import com.timolisa.fashionblogapi.dto.PostDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.repository.PostSpecification;
import com.timolisa.fashionblogapi.service.PostService;
import com.timolisa.fashionblogapi.util.ResponseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/fashion-blog/posts")
public class PostController {
    private final PostService postService;
    private final ResponseManager<Post> responseManager;

    @PostMapping("/new")
    public ResponseEntity<APIResponse<Post>> createPost(@RequestBody PostDTO postDTO)
            throws UnauthorizedAccessException, InvalidInputsException {
        APIResponse<Post> post = postService.createPost(postDTO);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<APIResponse<List<Post>>>
    search(@RequestParam(value = "title", required = false) String title,
           @RequestParam(value = "category", required = false) String category) {

        Specification<Post> specification = Specification.where(null);
        if (title != null && !title.isEmpty()) {
            specification = specification.and(PostSpecification.hasTitle(title));
        }
        if (category != null && !category.isEmpty()) {
            specification = specification.and(PostSpecification.hasCategory(category));
        }
        APIResponse<List<Post>> searchResults = postService.searchForPosts(specification);
        return ResponseEntity.ok().body(searchResults);
    }
    @GetMapping
    public ResponseEntity<APIResponse<Page<Post>>> getAllPosts(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            APIResponse<Page<Post>> response = postService.findAllPosts(pageable);
            return new ResponseEntity<>(response, HttpStatus.FOUND);
        } catch (PostNotFoundException | UnauthorizedAccessException e) {
            APIResponse<Page<Post>> response = responseManager.notFound(e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{post-id}")
    public ResponseEntity<APIResponse<Post>> viewPost(@PathVariable("post-id") Long postId)
            throws PostNotFoundException, UnauthorizedAccessException {
        APIResponse<Post> foundPost = postService.findPostById(postId);
        return new ResponseEntity<>(foundPost, HttpStatus.FOUND);
    }

    @PutMapping("/update/{post-id}")
    public ResponseEntity<APIResponse<Post>> editPost(@RequestBody PostDTO postDTO,
                                                      @PathVariable("post-id") Long postId)
            throws PostNotFoundException, UnauthorizedAccessException {
        APIResponse<Post> existingPost = postService.updatePost(postId, postDTO);
        return new ResponseEntity<>(existingPost, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/delete/{post-id}")
    public ResponseEntity<APIResponse<String>> deletePost(@PathVariable("post-id") Long postId)
            throws PostNotFoundException, UnauthorizedAccessException {
        APIResponse<String> response =
                postService.deletePost(postId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
