package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.CommentDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.enums.Role;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.repository.CommentRepository;
import com.timolisa.fashionblogapi.repository.PostRepository;
import com.timolisa.fashionblogapi.service.CommentService;
import com.timolisa.fashionblogapi.util.LoggedInUser;
import com.timolisa.fashionblogapi.util.ResponseManager;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final ResponseManager<Comment> responseManager;
    private final HttpSession session;
    private final LoggedInUser loggedInUser;
    private final EntityManager entityManager;
    @Override
    public ApiResponse<Comment> createComment(CommentDTO commentDTO, Long postId)
            throws InvalidInputsException, PostNotFoundException {
        Comment comment = new Comment();
        if (session.getAttribute("userId") == null) {
            User user = new User();
            user.setRole(Role.ANONYMOUS_USER);
            comment.setUser(user);
        } else {
            comment.setUser(loggedInUser.findLoggedInUser());
        }
        Post foundPost = postRepository.findById(postId)
                .orElseThrow(() -> {
                    String message = "Post not found";
                    return new PostNotFoundException(message);
                });

        if (commentDTO.getComment().equals("")) {
            throw new InvalidInputsException("Comments cannot be empty");
        }
//        comment.setPost(foundPost);
        BeanUtils.copyProperties(commentDTO, comment);
//        log.info("User comment:: {}", comment);

//        commentRepository.save(comment);
        saveComment(postId, comment);
        return responseManager.success(comment);
    }

    @Override
    @Transactional
    public void saveComment(Long postId, Comment comment) {
        Post post = entityManager.find(Post.class, postId);
        comment.setPost(post);
        commentRepository.save(comment);
    }

    @Override
    public ApiResponse<Comment> findCommentById(Long id) throws PostNotFoundException, UnauthorizedAccessException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Please login to the application");
        }
        Comment comment =  commentRepository.findById(id)
                .orElseThrow(() -> {
                    String message = "Comment Not found";
                    return new PostNotFoundException(message);
                });
        return responseManager.success(comment);
    }

    @Override
    public ApiResponse<List<Comment>> findAllCommentsForAPost(Long postId) throws UnauthorizedAccessException, PostNotFoundException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Please login to the application");
        }
        List<Comment> comments =
                commentRepository.findByPost_IdOrderByCreatedAt(postId);
        if (comments.size() == 0) {
            throw new PostNotFoundException("No comments for this post");
        }
        return responseManager.success(comments);
    }

    @Override
    public ApiResponse<Comment> updateComment(CommentDTO commentDTO, Long commentId) throws UnauthorizedAccessException, PostNotFoundException, InvalidInputsException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Please login to the application");
        }
        if (commentDTO.getComment().equals("")) {
            throw new InvalidInputsException("Please type in a comment");
        }
        Comment foundComment =
                commentRepository.findById(commentId).orElseThrow(() -> {
                            String message = "Comment not found";
                            return new PostNotFoundException(message);
                        });
        foundComment.setComment(commentDTO.getComment());
        commentRepository.save(foundComment);
        return responseManager.success(foundComment);
    }

    @Override
    public ApiResponse<String> deleteComment(Long commentId) throws UnauthorizedAccessException, PostNotFoundException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Please login to the application");
        }
        Comment foundComment =
                commentRepository.findById(commentId).orElseThrow(() -> {
                    String message = "Comment not found";
                    return new PostNotFoundException(message);
                });
        commentRepository.delete(foundComment);
        return responseManager.success("Comment deleted");
    }
}
