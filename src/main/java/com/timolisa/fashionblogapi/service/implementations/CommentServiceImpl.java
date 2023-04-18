package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.dto.CommentDTO;
import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.enums.Role;
import com.timolisa.fashionblogapi.exception.InvalidInputsException;
import com.timolisa.fashionblogapi.exception.PostNotFoundException;
import com.timolisa.fashionblogapi.exception.UnauthorizedAccessException;
import com.timolisa.fashionblogapi.repository.CommentRepository;
import com.timolisa.fashionblogapi.service.CommentService;
import com.timolisa.fashionblogapi.util.LoggedInUser;
import com.timolisa.fashionblogapi.util.ResponseManager;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ResponseManager<Comment> responseManager;
    private final HttpSession session;
    private final LoggedInUser loggedInUser;
    @Override
    public ApiResponse<Comment> createComment(CommentDTO commentDTO, Long postId) throws UnauthorizedAccessException, InvalidInputsException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Please login to the application");
        }
        if (loggedInUser
                .findLoggedInUser().getRole() != Role.ADMIN) {
            throw new UnauthorizedAccessException("You are not authorized to carry out this operation");
        }
        if (commentDTO.getComment().equals("")) {
            throw new InvalidInputsException("Comments cannot be empty");
        }
        Comment comment = new Comment();
        BeanUtils.copyProperties(commentDTO, comment);
        comment.setUser(loggedInUser.findLoggedInUser());
        commentRepository.save(comment);
        return responseManager.success(comment);
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
    public ApiResponse<List<Comment>> findAllCommentsForAPost(Long postId) throws UnauthorizedAccessException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Please login to the application");
        }
        List<Comment> comments =
                commentRepository.findByPost_IdOrderByCreatedAt(postId);
        return responseManager.success(comments);
    }

    @Override
    public ApiResponse<Comment> updateComment(CommentDTO commentDTO, Long commentId) throws UnauthorizedAccessException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Please login to the application");
        }
        return null;
    }

    @Override
    public ApiResponse<String> deleteComment(Long commentId) throws UnauthorizedAccessException {
        if (session.getAttribute("userId") == null) {
            throw new UnauthorizedAccessException("Please login to the application");
        }
        return null;
    }
}
