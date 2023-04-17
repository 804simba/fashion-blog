package com.timolisa.fashionblogapi.dto;

import com.timolisa.fashionblogapi.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Category category;
    private List<CommentDTO> comments;
    private List<LikeDTO> likes;
    private Long userId;
    private LocalDateTime updatedAt;
}
