package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.UserData;
import com.timolisa.fashionblogapi.dto.PostDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.enums.Category;
import com.timolisa.fashionblogapi.enums.Role;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.repository.PostRepository;
import com.timolisa.fashionblogapi.util.LoggedInUser;
import com.timolisa.fashionblogapi.util.ResponseManager;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PostServiceImplTest {
    @Mock
    private PostRepository postRepository;
    @Mock
    private ResponseManager<Post> responseManager;
    @Mock
    private HttpSession session;
    @Mock
    private LoggedInUser loggedInUser;
    @InjectMocks
    private PostServiceImpl postService;

    @Test
    public void testCreatePost() throws UnauthorizedAccessException, InvalidInputsException {
        User user = UserData.buildUser();
        user.setUserId(1L);
        user.setRole(Role.ADMIN);
        Post post = PostData.buildPost();
        PostDTO postDTO = PostData.buildPostDTO();

        when(session.getAttribute("userId"))
                .thenReturn(1L);
        when(loggedInUser.findLoggedInUser())
                .thenReturn(user);
        when(postRepository.save(any(Post.class)))
                .thenReturn(post);
        when(responseManager.success(any(Post.class)))
                .thenReturn(new ApiResponse<>("Post created successfully", true, post));

        ApiResponse<Post> result = postService.createPost(postDTO);
        assertEquals("example", result.getData().getTitle());
    }

    @Test
    public void testFindPostById() throws PostNotFoundException, UnauthorizedAccessException {
        Post post = PostData.buildPost();
        post.setId(1L);
        when(session.getAttribute("userId"))
                .thenReturn(1L);
        when(postRepository.existsById(1L))
                .thenReturn(true);
        when(postRepository.findById(any(Long.class)))
                .thenReturn(Optional.of(post));
        when(responseManager.success(any(Post.class)))
                .thenReturn(new ApiResponse<>("Post", true, post));
        ApiResponse<Post> response =
                postService.findPostById(1L);
        assertEquals(ApiResponse.class, response.getClass());
    }

    @Test
    void testFindAllPosts() throws Exception {
        Pageable pageable = PageRequest.of(1, 3);
        Page<Post> postsPage =
                new PageImpl<>(Arrays.asList(new Post(), new Post(), new Post()), pageable, 6);

        when(session.getAttribute("userId"))
                .thenReturn(1L);
        when(postRepository.findAll(pageable)).thenReturn(postsPage);
        when(responseManager.success(postsPage))
                .thenReturn(new ApiResponse<>("Success", true, postsPage));

        ApiResponse<Page<Post>> response = postService.findAllPosts(pageable);

        assertEquals(ApiResponse.class, response.getClass());
        assertEquals("Success", response.getMessage());
        assertEquals(postsPage, response.getData());
    }

    public static class PostData {
        public static Post buildPost() {
            User user = UserData.buildUser();
            user.setUserId(1L);
            return Post.builder()
                    .title("example")
                    .content("Lorem ipsum")
                    .category(Category.MALE)
                    .user(user)
                    .build();
        }

        public static PostDTO buildPostDTO() {
            User user = UserData.buildUser();
            user.setUserId(1L);
            return PostDTO.builder()
                    .title("example")
                    .content("Lorem ipsum")
                    .category(Category.MALE)
                    .userId(user.getUserId())
                    .build();
        }
    }
}