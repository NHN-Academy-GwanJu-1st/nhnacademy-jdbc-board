package com.nhnacademy.controller;

import com.nhnacademy.service.CommentService;
import org.springframework.stereotype.Controller;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }


}
