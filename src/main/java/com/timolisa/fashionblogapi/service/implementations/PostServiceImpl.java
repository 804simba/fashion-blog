package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.PostDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UserDoesNotExistException;
import com.timolisa.fashionblogapi.repository.PostRepository;
import com.timolisa.fashionblogapi.repository.UserRepository;
import com.timolisa.fashionblogapi.service.PostService;
import com.timolisa.fashionblogapi.security.jwt.JwtTokenProvider;
import com.timolisa.fashionblogapi.utils.ResponseManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ResponseManager<Post> responseManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public APIResponse<Post> createPost(PostDTO postDto)
            throws InvalidInputsException, UserDoesNotExistException {
        String token = jwtTokenProvider.getTokenFromContext();
        Long userId = jwtTokenProvider.getUserIdFromToken(token);

        if (postDto.getTitle().equals("") || postDto.getContent().equals("")
                || postDto.getCategory() == null) {
            throw new InvalidInputsException("You are missing one of the required fields");
        }

        Post post = new Post();
        BeanUtils.copyProperties(postDto, post);
        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserDoesNotExistException("User does not exist with id: "+userId));
        log.info("User that made new post:: {}", user);
        post.setUser(user);
        postRepository.save(post);

        return responseManager.success(post);
    }

    @Override
    public APIResponse<Post> findPostById(Long postId)
            throws PostNotFoundException {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> {
                    String message = "Post not found";
                    return new PostNotFoundException(message);
                });
        return responseManager.success(post);
    }

    @Override
    public APIResponse<Page<Post>> findAllPosts(Pageable pageable)
            throws PostNotFoundException {
        Page<Post> pages = postRepository.findAll(pageable);
        if (pages.isEmpty()) {
            throw new PostNotFoundException("Posts not found");
        }
        return responseManager.success(pages);
    }

    @Override
    public APIResponse<Post> updatePost(Long postId, PostDTO postDTO)
            throws PostNotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found.");
        }

        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException("Post not found with id: " + postId));

//        BeanUtils.copyProperties(postDTO, foundPost);
        foundPost.setTitle(postDTO.getTitle());
        foundPost.setContent(postDTO.getContent());
        foundPost.setCategory(postDTO.getCategory());
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
            throws PostNotFoundException {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException("Post not found");
        }

        postRepository
                .delete(postRepository.findById(postId)
                        .orElseThrow(() -> new PostNotFoundException("Post not found for delete operation")));
        return responseManager.success("Post deleted");
    }
}
