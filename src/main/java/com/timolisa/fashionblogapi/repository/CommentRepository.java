package com.timolisa.fashionblogapi.repository;

import com.timolisa.fashionblogapi.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository
        extends JpaRepository<Comment, Long> {
    List<Comment> findByPost_IdOrderByCreatedAt(Long postId);
}
