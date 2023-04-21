package com.timolisa.fashionblogapi.repository;

import com.timolisa.fashionblogapi.entity.Post;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class PostSpecification {
    // https://blog.piinalpin.com/2022/04/searching-and-filtering-using-jpa-specification/
    // https://www.baeldung.com/rest-api-search-language-spring-data-specifications
    // https://medium.com/@cmmapada/advanced-search-and-filtering-using-spring-data-jpa-specification-and-criteria-api-b6e8f891f2bf
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
