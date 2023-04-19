package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Like;
import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.repository.LikeRepository;
import com.timolisa.fashionblogapi.service.LikeService;
import com.timolisa.fashionblogapi.util.ResponseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikeRepository likeRepository;
    private final ResponseManager<Like> responseManager;
    @Override
    public ApiResponse<Like> createLikeForComment(Comment comment) {
        Like like = new Like();
        like.setComment(comment);
        likeRepository.save(like);
        return responseManager.success(like);
    }

    @Override
    public ApiResponse<String> deleteLikeForComment(Long commentId) {
        likeRepository.deleteByComment_Id(commentId);
        String message = String.format("like for post with id: %d has been unliked", commentId);
        return responseManager.success(message);
    }

    @Override
    public ApiResponse<Like> createLikeForPost(Post post) {
        Like like = new Like();
        like.setPost(post);
        likeRepository.save(like);
        return responseManager.success(like);
    }

    @Override
    public ApiResponse<String> deleteLikeForPost(Long postId) {
        likeRepository.deleteByPost_Id(postId);
        String message = String.format("like for post with id: %d has been unliked", postId);
        return responseManager.success(message);
    }
}
