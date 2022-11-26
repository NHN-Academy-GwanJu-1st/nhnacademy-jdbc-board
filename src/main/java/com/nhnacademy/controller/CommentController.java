package com.nhnacademy.controller;

import com.nhnacademy.domain.CommentRegisterRequest;
import com.nhnacademy.domain.User;
import com.nhnacademy.exception.ValidationFailedException;
import com.nhnacademy.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/register/{boardId}")
    public String registerComment(@PathVariable(value = "boardId") long boardId,
                                  @Valid @ModelAttribute(value = "comment") CommentRegisterRequest commentRequest,
                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }
        commentService.registerComment(commentRequest);

        return "redirect:/board/" + boardId;
    }

    @PostMapping("/modify/{commentId}")
    public String modifyComment(@PathVariable(value = "commentId") long commentId,
                                @ModelAttribute(value = "comment") CommentRegisterRequest commentRequest,
                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        commentService.modifyComment(commentId, commentRequest.getContent());


        return "redirect:/board/" + commentRequest.getBoardId();
    }

    @PostMapping("/delete/{commentId}")
    public String deleteComment(@PathVariable(value = "commentId") long commentId,
                                @RequestParam(value = "boardId") long boardId) {

        commentService.deleteComment(commentId);

        return "redirect:/board/" + boardId;
    }
}
