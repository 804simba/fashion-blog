package com.timolisa.fashionblogapi.utils;

import com.timolisa.fashionblogapi.service.UserService;
import com.timolisa.fashionblogapi.service.implementations.UserServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

import static com.timolisa.fashionblogapi.config.SecurityConfig.WHITE_LIST_URLS;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * The OncePerRequestFilter is a base class for Filter implementations that
 * guarantees to be executed only once per request.
 * doFilterInternal() is responsible for performing the filtering logic
 * doFilter() is responsible for checking whether the request has been filtered or not
 * if the request has not been filtered, it calls doFilterInternal();
 * otherwise, it does nothing and returns i.e. if the request has been filtered.
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class JWTAuthorizationFilter extends OncePerRequestFilter {
    private static final String TOKEN_PREFIX = "Bearer ";
    private final JwtTokenProvider jwtTokenProvider;
    private final ApplicationContext applicationContext;

    private boolean isUrlInWhiteList(HttpServletRequest request) {
        String requestUrl = request.getRequestURI();
        return Arrays.stream(WHITE_LIST_URLS)
                .anyMatch(requestUrl::startsWith);
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (isUrlInWhiteList(request)) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
                String token = authorizationHeader.substring(TOKEN_PREFIX.length());
                String username = jwtTokenProvider.getUsernameFromToken(token);
                UserDetails userDetails = getUserDetails(username);
                if (jwtTokenProvider.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    private UserDetails getUserDetails(String username) {
        UserService userService = applicationContext.getBean(UserServiceImpl.class);
        return userService.loadByUsername(username);
    }
}