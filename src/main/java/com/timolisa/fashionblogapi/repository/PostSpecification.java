package com.timolisa.fashionblogapi.repository;

import com.timolisa.fashionblogapi.entity.Post;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PostSpecification {
    public static Specification<Post> hasTitle(String title) {

        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("title")), "%" + title + "%"));
    }
    public static Specification<Post> hasCategory(String category) {
        return ((root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("category")), category.toLowerCase()));
    }
}
