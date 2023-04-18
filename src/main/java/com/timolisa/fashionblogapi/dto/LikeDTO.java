package com.timolisa.fashionblogapi.dto;

import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.entity.User;
import lombok.Data;

@Data
public class LikeDTO {
    private Long numberOfLikes = 0L;
    private User user;
    private Post post;
}
