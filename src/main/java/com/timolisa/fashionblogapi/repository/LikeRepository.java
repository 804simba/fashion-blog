package com.timolisa.fashionblogapi.repository;

import com.timolisa.fashionblogapi.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository
        extends JpaRepository<Like, Long> {
    void deleteByComment_Id(Long commentId);
    void deleteByPost_Id(Long postId);
}
