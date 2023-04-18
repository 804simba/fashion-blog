package com.timolisa.fashionblogapi.controller;

import com.timolisa.fashionblogapi.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fashion-blog/post/")
public class CommentController {
    private final CommentService commentService;

}
