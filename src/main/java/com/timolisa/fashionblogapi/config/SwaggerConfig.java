package com.timolisa.fashionblogapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

//    @Value("${application.name}")
//    private String version;
    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .info(new Info()
                        .title("Fashion Blog API")
                        .description("A RestFUL web-service that provides crud operations for a fashion blog"));
//                        .version(version));
    }

    @Bean
    public GroupedOpenApi usersEndPoints() {
        return GroupedOpenApi.builder()
                .group("Users")
                .pathsToMatch("/api/fashion-blog/users/**")
                .build();
    }

    @Bean
    public GroupedOpenApi postsEndPoints() {
        return GroupedOpenApi.builder()
                .group("Posts")
                .pathsToMatch("/api/fashion-blog/posts/**")
                .build();
    }

    @Bean
    public GroupedOpenApi commentsEndPoint() {
        return GroupedOpenApi.builder()
                .group("Comments")
                .pathsToMatch("/api/fashion-blog/post/comments/**")
                .build();
    }

    @Bean
    public GroupedOpenApi likesEndPoints() {
        return GroupedOpenApi.builder()
                .group("Likes")
                .pathsToMatch("/api/fashion-blog/comment/**")
                .build();
    }
}
