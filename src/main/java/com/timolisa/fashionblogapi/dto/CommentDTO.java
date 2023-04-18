package com.timolisa.fashionblogapi.dto;

import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDTO {
    private String comment;
    private User user;
    private Post post;
    private Timestamp createdAt;
}
