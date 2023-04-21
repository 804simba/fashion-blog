package com.timolisa.fashionblogapi.util;

import com.timolisa.fashionblogapi.entity.Post;
import com.timolisa.fashionblogapi.enums.Category;
import jakarta.persistence.metamodel.SingularAttribute;
import jakarta.persistence.metamodel.StaticMetamodel;

@StaticMetamodel(Post.class)
public abstract class Post_ {
    public static SingularAttribute<Post, String> TITLE;
    public static SingularAttribute<Post, Category> CATEGORY;
}
