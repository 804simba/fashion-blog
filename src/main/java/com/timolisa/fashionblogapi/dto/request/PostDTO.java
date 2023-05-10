package com.timolisa.fashionblogapi.dto.request;

import com.timolisa.fashionblogapi.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Category category;
    private List<CommentDTO> comments;
    private List<LikeDTO> likes;
//    private Long userId;
}
