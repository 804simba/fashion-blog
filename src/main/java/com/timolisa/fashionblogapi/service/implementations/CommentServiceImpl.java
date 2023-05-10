package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.request.CommentDTO;
import com.timolisa.fashionblogapi.entity.APIResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.entity.User;
import com.timolisa.fashionblogapi.enums.Role;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UserDoesNotExistException;
import com.timolisa.fashionblogapi.repository.CommentRepository;
import com.timolisa.fashionblogapi.repository.PostRepository;
import com.timolisa.fashionblogapi.repository.UserRepository;
import com.timolisa.fashionblogapi.service.CommentService;
import com.timolisa.fashionblogapi.security.jwt.JwtTokenProvider;
import com.timolisa.fashionblogapi.utils.ResponseManager;
import jakarta.persistence.EntityManager;
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
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ResponseManager<Comment> responseManager;
    private final EntityManager entityManager;
    private final JwtTokenProvider jwtTokenProvider;
    @Override
    public APIResponse<Comment> createComment(CommentDTO commentDTO, Long postId)
            throws InvalidInputsException, PostNotFoundException, UserDoesNotExistException {
        Comment comment = new Comment();
        String token = jwtTokenProvider.getTokenFromContext();
        Long userId = jwtTokenProvider.getUserIdFromToken(token);
        User user;

        if (userId == null) {
            user = new User();
            user.setRole(Role.ANONYMOUS_USER);
            comment.setUser(user);
        } else {
            user = userRepository.findById(userId).orElseThrow(() -> {
                        String message = "User not found";
                        return new UserDoesNotExistException(message);
                    });
            comment.setUser(user);
        }

        Post foundPost = postRepository.findById(postId).orElseThrow(() -> {
                    String message = "Post not found";
                    return new PostNotFoundException(message);
                });

        if (commentDTO.getComment().equals("")) {
            throw new InvalidInputsException("Comments cannot be empty");
        }

        comment.setPost(foundPost);
        BeanUtils.copyProperties(commentDTO, comment);
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
    public APIResponse<Comment> findCommentById(Long id) throws PostNotFoundException {
        Comment comment =  commentRepository.findById(id).orElseThrow(() -> {
                    String message = "Comment Not found";
                    return new PostNotFoundException(message);
                });
        return responseManager.success(comment);
    }

    @Override
    public APIResponse<List<Comment>> findAllCommentsForAPost(Long postId) throws PostNotFoundException {
        List<Comment> comments =
                commentRepository.findByPost_IdOrderByCreatedAt(postId);
        if (comments.size() == 0) {
            throw new PostNotFoundException("No comments for this post");
        }
        return responseManager.success(comments);
    }

    @Override
    public APIResponse<Comment> updateComment(CommentDTO commentDTO, Long commentId) throws PostNotFoundException, InvalidInputsException {
        if (commentDTO.getComment().equals("")) {
            throw new InvalidInputsException("Please type in a comment");
        }

        Comment foundComment = commentRepository.findById(commentId).orElseThrow(() -> {
                            String message = "Comment not found";
                            return new PostNotFoundException(message);
                        });

        foundComment.setComment(commentDTO.getComment());
        commentRepository.save(foundComment);
        return responseManager.success(foundComment);
    }

    @Override
    public APIResponse<String> deleteComment(Long commentId) throws PostNotFoundException {
        Comment foundComment = commentRepository.findById(commentId).orElseThrow(() -> {
                    String message = "Comment not found";
                    return new PostNotFoundException(message);
                });

        commentRepository.delete(foundComment);
        return responseManager.success("Comment deleted");
    }
}
