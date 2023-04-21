package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.PostDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.enums.Role;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.repository.PostRepository;
import com.timolisa.fashionblogapi.service.PostService;
import com.timolisa.fashionblogapi.util.LoggedInUser;
import com.timolisa.fashionblogapi.util.ResponseManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final ResponseManager<Post> responseManager;
    private final HttpSession session;
    private final LoggedInUser loggedInUser;

    @Override
    public APIResponse<Post> createPost(PostDTO postDto)
            throws UnauthorizedAccessException, InvalidInputsException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Please login to the application.");
        }
        if (loggedInUser.findLoggedInUser().getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You are not authorized to carry out this operation");
        }
        if (postDto.getTitle().equals("") || postDto.getContent().equals("")
                || postDto.getCategory() == null) {
            throw new InvalidInputsException("You are missing one of the required fields");
        }
        Post post = new Post();
        BeanUtils.copyProperties(postDto, post);
        post.setUser(loggedInUser.findLoggedInUser());
        postRepository.save(post);

        return responseManager.success(post);
    }

    @Override
    public APIResponse<Post> findPostById(Long postId)
            throws PostNotFoundException, UnauthorizedAccessException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Login to the application");
        }
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found.");
        }
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    String message = "Post not found";
                    return new PostNotFoundException(message);
                });
        return responseManager.success(post);
    }

    @Override
    public APIResponse<Page<Post>> findAllPosts(Pageable pageable)
            throws PostNotFoundException, UnauthorizedAccessException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Login to the application");
        }
        Page<Post> pages = postRepository.findAll(pageable);
        if (pages.isEmpty()) {
            throw new PostNotFoundException("Posts not found");
        }
        return responseManager.success(pages);
    }

    @Override
    public APIResponse<Post> updatePost(Long postId, PostDTO postDTO)
            throws UnauthorizedAccessException, PostNotFoundException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Login to the application");
        }
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found.");
        }
        if (loggedInUser.findLoggedInUser().getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You are not authorized to carry out this operation.");
        }
        Post foundPost = postRepository.findById(postId).orElse(null);
        assert foundPost != null;
        BeanUtils.copyProperties(postDTO, foundPost);
        postRepository.save(foundPost);
        return responseManager.success(foundPost);
    }

    @Override
    public APIResponse<List<Post>> searchForPosts(Specification<Post> specification) {
        List<Post> searchResults = postRepository.findAll(specification);
        return responseManager.success(searchResults);
    }

    @Override
    public APIResponse<String> deletePost(Long postId)
            throws UnauthorizedAccessException, PostNotFoundException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Login to the application");
        }
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found");
        }
        if (loggedInUser.findLoggedInUser().getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You are not authorized to carry out this operation.");
        }
        postRepository
                .delete(postRepository.findById(postId).get());
        return responseManager.success("Post deleted");
    }
}
