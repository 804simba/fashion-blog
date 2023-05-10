package com.timolisa.fashionblogapi.dto.request;

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
//    private Long userId;
//    private Long postId;
//    private Timestamp createdAt;
}
