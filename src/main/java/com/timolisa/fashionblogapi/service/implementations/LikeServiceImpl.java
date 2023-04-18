package com.timolisa.fashionblogapi.service.implementations;

import com.timolisa.fashionblogapi.entity.ApiResponse;
import com.timolisa.fashionblogapi.entity.Comment;
import com.timolisa.fashionblogapi.entity.Like;
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
        return responseManager.success("like deleted successfully");
    }
}
