package com.timolisa.fashionblogapi.dto;

import lombok.Data;

@Data
public class LikeDTO {
    private Long numberOfLikes = 0L;
    private Long userId;
    private Long postId;
    private Long commentId;
}
