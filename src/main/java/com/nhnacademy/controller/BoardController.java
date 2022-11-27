package com.nhnacademy.controller;

import com.nhnacademy.domain.*;
import com.nhnacademy.exception.UserNotAllowedException;
import com.nhnacademy.exception.ValidationFailedException;
import com.nhnacademy.service.BoardService;
import com.nhnacademy.service.CommentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Slf4j
@Controller
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;
    private final CommentService commentService;

    public BoardController(BoardService boardService, CommentService commentService) {
        this.boardService = boardService;
        this.commentService = commentService;
    }

    @GetMapping("/register")
    public String getBoardRegisterForm() {
        return "/board/registerForm";
    }

    @PostMapping("/register")
    public String doBoardRegister(@Valid @ModelAttribute(value = "board") BoardRegisterRequest boardRequest,
                                  BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        boardService.registerBoard(
                boardRequest.getUserName(),
                boardRequest.getTitle(),
                boardRequest.getContent()
        );

        return "redirect:/";
    }

    @GetMapping("/{boardId}")
    public String getBoardDetail(@PathVariable(value = "boardId") long boardId,
                                 Model model) {

        Board board = boardService.findById(boardId);
        List<Comment> commentList = commentService.findByBoardId(boardId);
        model.addAttribute("board", board);
        model.addAttribute("commentList", commentList);

        return "/board/detail";
    }

    @GetMapping("/modify/{boardId}")
    public String getModifyForm(@PathVariable(value = "boardId") long boardId,
                                @SessionAttribute(value = "user") UserVO userSession,
                                Model model) {

        if (!boardService.allowedUserCheck(boardId, userSession)) {
            throw new UserNotAllowedException();
        }

        Board board = boardService.findById(boardId);
        model.addAttribute("board", board);

        return "/board/modify";
    }

    @PostMapping("/modify/{boardId}")
    public String doModify(@PathVariable(value = "boardId") long boardId,
                           @Valid @ModelAttribute(value = "board") BoardRegisterRequest boardRequest,
                           @SessionAttribute(value = "user") UserVO userSession,
                           BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationFailedException(bindingResult);
        }

        if (!boardService.allowedUserCheck(boardId, userSession)) {
            throw new UserNotAllowedException();
        }

        boardService.modifyBoard(
                boardId,
                boardRequest.getUserName(),
                boardRequest.getTitle(),
                boardRequest.getContent()
        );

        return "redirect:/";
    }

    @PostMapping("/delete/{boardId}")
    public String doDelete(@PathVariable(value = "boardId") long boardId,
                           @SessionAttribute(value = "user") UserVO userSession) {

        if (!boardService.allowedUserCheck(boardId, userSession)) {
            throw new UserNotAllowedException();
        }

        boardService.deleteBoard(boardId);


        return "redirect:/";
    }

    @PostMapping("/recover/{boardId}")
    public String doBoardRecover(@PathVariable(value = "boardId") long boardId,
                            @SessionAttribute(value = "user") UserVO loginUser) {

        if (!loginUser.getRole().equals("Admin")) {
            throw new UserNotAllowedException();
        }

        boardService.recoverBoard(boardId);

        return "redirect:/";
    }
}
