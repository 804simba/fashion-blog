package com.timolisa.fashionblogapi.repository;

import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.util.Post_;
import org.springframework.data.jpa.domain.Specification;

public class PostSpecification {
    public static Specification<Post> hasTitle(String title) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(Post_.TITLE)), "%" + title + "%"));
    }
    public static Specification<Post> hasCategory(String category) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get(String.valueOf(Post_.CATEGORY))), category.toLowerCase()));
    }
}
