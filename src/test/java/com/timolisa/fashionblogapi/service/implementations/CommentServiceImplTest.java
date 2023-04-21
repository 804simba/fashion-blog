package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.UserData;
import com.timolisa.fashionblogapi.dto.CommentDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.enums.Role;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.repository.CommentRepository;
import com.timolisa.fashionblogapi.repository.PostRepository;
import com.timolisa.fashionblogapi.util.LoggedInUser;
import com.timolisa.fashionblogapi.util.ResponseManager;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ResponseManager<Comment> responseManager;
    @Mock
    private HttpSession session;
    @Mock
    private LoggedInUser loggedInUser;
    @InjectMocks
    private CommentServiceImpl commentService;

    @Test
    public void testCreateComment() throws PostNotFoundException, UnauthorizedAccessException, InvalidInputsException {
        User user = UserData.buildUser();
        user.setRole(Role.USER);
        Post post = PostServiceImplTest.PostData.buildPost();
        CommentDTO commentDTO = CommentData.buildCommentDTO();
        Comment comment = CommentData.buildComment();

        when(session.getAttribute("userId"))
                .thenReturn(user);
        when(loggedInUser.findLoggedInUser())
                .thenReturn(user);
        when(postRepository.findById(1L))
                .thenReturn(Optional.of(post));
        when(commentRepository.save(any(Comment.class)))
                .thenReturn(comment);
        when(responseManager.success(any(Comment.class)))
                .thenReturn(new APIResponse<>("Comment successful", true, comment));
        Long postId = commentDTO.getPost().getId();

        APIResponse<Comment> result = commentService
                .createComment(commentDTO, postId);

        assertEquals("Love it", result.getPayload().getComment());
    }

    public static class CommentData {
        public static Comment buildComment() {
            User user = UserData.buildUser();
            Post post = PostServiceImplTest.PostData.buildPost();
            post.setId(1L);
            user.setUserId(1L);
            return Comment.builder()
                    .comment("Love it")
                    .user(user)
                    .post(post)
                    .build();
        }

        public static CommentDTO buildCommentDTO() {
            User user = UserData.buildUser();
            Post post = PostServiceImplTest.PostData.buildPost();
            post.setId(1L);
            user.setUserId(1L);
            return CommentDTO.builder()
                    .comment("Love it")
                    .user(user)
                    .post(post)
                    .build();
        }
    }
}