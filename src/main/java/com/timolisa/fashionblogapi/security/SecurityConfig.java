package com.timolisa.fashionblogapi.security;

import com.timolisa.fashionblogapi.enums.Role;
import com.timolisa.fashionblogapi.security.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationProvider authenticationProvider;
    private final JwtAuthorizationFilter jwtAuthorizationFilter;

    public static final String[] WHITE_LIST_URLS = {
            "/api/fashion-blog/auth/user/login",
            "/api/fashion-blog/auth/user/sign-up",
            "/api/fashion-blog/auth/admin/sign-up",
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    @Bean
    public SecurityFilterChain getSecurityFilterChain(HttpSecurity http) throws Exception {
        log.info("Before filter chain");
        http.csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS)
                .and().authorizeHttpRequests().requestMatchers(WHITE_LIST_URLS).permitAll()
                .and().authorizeHttpRequests().requestMatchers(POST, "/api/fashion-blog/posts/**").hasAuthority("ADMIN")
                .and().authorizeHttpRequests().requestMatchers(PUT, "/api/fashion-blog/posts/**").hasAuthority(String.valueOf(Role.ADMIN))
                .and().authorizeHttpRequests().requestMatchers(DELETE, "/api/fashion-blog/posts/**").hasAuthority(String.valueOf(Role.ADMIN))
                .and().authorizeHttpRequests().requestMatchers(POST, "/api/fashion-blog/post/comment/**").hasAnyAuthority(String.valueOf(Role.ADMIN), String.valueOf(Role.USER))
                .and().authorizeHttpRequests().requestMatchers(PUT, "/api/fashion-blog/post/comment/**").hasAnyAuthority(String.valueOf(Role.ADMIN), String.valueOf(Role.USER))
                .and().authorizeHttpRequests().requestMatchers(DELETE, "/api/fashion-blog/post/comment/**").hasAnyAuthority(String.valueOf(Role.ADMIN), String.valueOf(Role.USER))
                .and().authorizeHttpRequests().requestMatchers("/api/fashion-blog/comment/**").hasAnyAuthority(String.valueOf(Role.ADMIN), String.valueOf(Role.USER))
                .and().authorizeHttpRequests().requestMatchers(GET, "/api/fashion-blog/post/comment/**").hasAnyAuthority(String.valueOf(Role.ADMIN), String.valueOf(Role.USER), String.valueOf(Role.ANONYMOUS_USER))
                .and().authorizeHttpRequests().requestMatchers(GET, "/api/fashion-blog/posts/**").hasAnyAuthority(String.valueOf(Role.ADMIN), String.valueOf(Role.USER), String.valueOf(Role.ANONYMOUS_USER))
                .and().authorizeHttpRequests().anyRequest().authenticated()
                .and()
                .authenticationProvider(authenticationProvider) // injects the authProvider
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class); // adds the filter before each request
                log.info("After filter chain");
        return http.build();
    }
}
